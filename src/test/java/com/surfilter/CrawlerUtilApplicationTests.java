package com.surfilter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.surfilter.config.RedisKeyInfo;
import com.surfilter.dao.CountryMapper;
import com.surfilter.dao.InfoMapper;
import com.surfilter.dao.IpMapper;
import com.surfilter.dao.WhiteUrlMapper;
import com.surfilter.entity.*;
import com.surfilter.job.FiedlityJob;
import com.surfilter.service.IIpService;
import com.surfilter.util.FileUtil;
import com.surfilter.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

@SuppressWarnings("ALL")
@SpringBootTest
@Slf4j
class CrawlerUtilApplicationTests {
    @Autowired
    IpMapper ipMapper;
    @Autowired
    WhiteUrlMapper whiteUrlMapper;
    @Autowired
    CountryMapper countryMapper;
    @Autowired
    RedisKeyInfo redisKeyInfo;
    @Autowired
    InfoMapper infoMapper;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    FiedlityJob fiedlityJob;
    @Autowired
    IIpService ipService;
    @Test
    public void infotest(){
        /***
         *
         *
         */


//
//        String ip = "123.140.238.53";
////        String gadd= "";
////        Long num = StringUtil.getIpNum( ip );
////        System.out.println(num);
////        Set<String> adds = stringRedisTemplate.opsForZSet().rangeByScore( redisKeyInfo.getFidelityIp(),num , Long.MAX_VALUE, 0, 1 );
////        for (String address : adds) {
////            gadd = address;
////            break;
////        }
////        System.out.println(gadd);
        int count = 0;
        for (; ; ) {
            List<Ip> list = ipService.listIps( count );
            if (list.size() > 0) {
                Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
                for (Ip ip : list) {
                    ZSetOperations.TypedTuple<String> tuple = new DefaultTypedTuple<String>( ip.getAddress() +
                            "_" + ip.getSpecificAddress()+ "_" + ip.getStartIpNum() +"_"+ip.getEndIpNum() + "_"+ip.getId(), (double) (ip.getEndIpNum()));
                    tuples.add( tuple );
                }
                stringRedisTemplate.opsForZSet().add( redisKeyInfo.getFidelityIp(), tuples );
                tuples.clear();
                tuples = null;
            }
            if (list.size() < 10000) {
                count += list.size();
                log.info( "存真ip入库成功，总条数：{}", count );
                break;
            } else {
                log.info( "存真ip入库成功入缓存，数量：{}", count );
                count += 10000;
            }
        }

    }




    @Test
    public void info(){
        List<CountryInfo>  list = countryMapper.listCountryInfo();
        System.out.println(list);
        System.out.println(1);

        for (;;) {
            List<Info> listinfo = infoMapper.listInfo1();
            for (Info info:listinfo) {
                String add = "";
                Set<String> adds = stringRedisTemplate.opsForZSet().rangeByScore( redisKeyInfo.getFidelityIp(), StringUtil.getIpNum( info.getIp() ), Long.MAX_VALUE, 0, 1 );
                for (String address : adds) {
                    add = address;
                    break;
                }
                adds.clear();
                if (StringUtils.isNotBlank( add )) {
                    for (CountryInfo cif : list) {
                        if (StringUtils.isNotBlank( cif.getProvince() ) && add.contains( cif.getProvince() )) {
                            info.setLngX( cif.getLngX() );
                            info.setLngY( cif.getLngY() );
                            info.setAddress( cif.getProvince() );
                            break;
                        }
                        if (StringUtils.isNotBlank( cif.getCountryCn()  ) && add.contains( cif.getCountryCn() )) {
                            info.setLngX( cif.getLngX() );
                            info.setLngY( cif.getLngY() );
                            info.setAddress( cif.getCountryCn() );
                            break;
                        }

                    }
                }
            }
            infoMapper.updateInfo(listinfo);
        }


    }




    @Test
    void tes1t(){
        //钓鱼网站黑名单。
       List<String> list =  FileUtil.ReadFileList1( "G:\\aaa.txt" );
       int count = 0 ;
       List<Phishing> listphishing = new ArrayList<>(  );
       List<String> domains = new ArrayList<>(  );
       for (String str :list) {
           count ++;
           if (!StringUtils.isNotBlank(str)) {
               continue;
           }
           String domain = getHost(str);

           if (!StringUtils.isNotBlank(domain)) {
              continue;
           }
           if (stringRedisTemplate.opsForSet().isMember( redisKeyInfo.getPhishingUrl(),domain )) {
               continue;
           }
           Phishing ph = new Phishing();
           if (str.length()>4000) {
               str = str.substring( 0,4000 );
           }
           ph.setUrl( str );
           if (domain.length()>=4000) {
               domain = str.substring( 0,4000 );
           }
           ph.setDomain( domain );
           listphishing.add( ph );
           domains.add( domain );
           if (listphishing.size() > 1000) {
               try {
                   whiteUrlMapper.addPhishing( listphishing );
                   String[] strings = new String[domains.size()];
                   domains.toArray( strings );
                   stringRedisTemplate.opsForSet().add( redisKeyInfo.getPhishingUrl(), strings );
                   listphishing.clear();
                   domains.clear();
               } catch (Exception e) {
                    e.printStackTrace();
               }
               count = 0;
           }
       }
       try {
            whiteUrlMapper.addPhishing( listphishing );
            String[] strings = new String[domains.size()];
            domains.toArray( strings );
            stringRedisTemplate.opsForSet().add( redisKeyInfo.getPhishingUrl(), strings );
            listphishing.clear();
            domains.clear();
       } catch (Exception e) {
            e.printStackTrace();
       }

    }


    /** 获得网站主机部分 */
    private static String getHost(String urlStr) {
        try {
            String host = new URL(urlStr).getHost();
            if (host.startsWith( "www." ) ){
                host = host.substring( 4,host.length() );
            }

            return host;
        } catch (Exception e) {
        }
        return "";
    }


    @Test
    void incountryInfoCaptal() {
        List<Map<String, String>> lmap = FileUtil.ReadFileList( "F:\\aaa.txt" );

        for (Map<String, String> map : lmap) {

            Set<String> key = map.keySet();
            List<String> list = new ArrayList<>( key );
            String newKey = list.get( 0 );
            System.out.println( newKey );

            String value = map.get( newKey );
            System.out.println( value );
            int count = countryMapper.getCount( newKey.trim() );
            if (count >= 1) {
                map.clear();
                map.put( "key", newKey );
                map.put( "value", value );
                countryMapper.updateCountryCaptical( map );
            } else {
                countryMapper.insertCountryInfo( map );
            }
            map.clear();
        }
        //countryMapper.insertCountry( list );
    }

    @Test
    void white() {
        List<String> list = FileUtil.ReadFileList1( "G:\\aliyun-safe-match\\data\\fish\\所有白名单.csv" );
        List<WhiteUrl> whiteUrls = new ArrayList<>();
        int count = 0;
        for (String str : list) {
            String[] ars = str.split( "," );
            if (ars.length > 1) {
                WhiteUrl whiteUrl = new WhiteUrl();
                String name = ars[0];
                String url = ars[1];
                if (url.startsWith( "www." )) {
                    url = url.substring( 4, url.length() );
                }
                whiteUrl.setName( name );
                whiteUrl.setUrl( url );
                whiteUrls.add( whiteUrl );
                if (whiteUrls.size() > 1000) {
                    whiteUrlMapper.insertWhiteUrl( whiteUrls );
                    whiteUrls.clear();
                }
            }
        }
        whiteUrlMapper.insertWhiteUrl( whiteUrls );

    }

    @Test
    void incountryInfo() {
        String str = FileUtil.ReadFile( "C:\\Users\\Administrator\\Desktop\\country-code-master\\country-code.json" );
        JSONArray array = JSON.parseArray( str );
        System.out.println( array );
        List<CountryInfo> list = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {
            JSONObject job = array.getJSONObject( i );  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
            //System.out.println(job.get("name")+"=") ;  // 得到 每个对象中的属性值
            CountryInfo ci = new CountryInfo();
            ci.setCountryCn( job.get( "cn" ).toString() );
            ci.setCountryEn( job.get( "en" ).toString() );
            list.add( ci );
        }
        countryMapper.insertCountry( list );
    }

    @Test
    void contextLoads() throws Exception {
        InputStreamReader isr = null;
        BufferedReader br = null;
        File file = new File("D:/tt2.txt");
        isr = new InputStreamReader(new FileInputStream(file), "GBK");
        br = new BufferedReader(isr);
        String str;
        // 按行读取字符串
        int count = 0 ;
        boolean flag = true;
        List<Ip> lista  = new ArrayList<Ip>(  );

        while ((str = br.readLine()) != null) {// 使用readLine方法，一次读一行
            str = new String(getUTF8BytesFromGBKString(str),"UTF-8");

            count ++;
            String start_ip = "";
            long strat_ip_num = 0;
            String end_ip = "";
            long end_ip_num = 0;
            String prince = "";
            String address = "";
            String specific_address = "";
            // 带有 IANA机构, IANA
            if (str.contains("IP数据库共有数据") || str.equals("")) {
                continue;
            }
            List<String> list = new ArrayList<String>();
            String[] strs = str.split(" ");
            for (int i = 0; i < strs.length; i++) {
                if (!strs[i].equals("")) {
                    list.add(strs[i]);
                }
            }
            Ip ip = new Ip();
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    start_ip = list.get(0);
                    ip.setStartIp( start_ip );
                   // log.info("开始ip:" + list.get(0));
                    strat_ip_num = StringUtil.getIpNum(list.get(0));
                    ip.setStartIpNum( strat_ip_num );
                    //log.info("结束ipNum:" + strat_ip_num);
                }
                if (i == 1) {
                    end_ip = list.get(1);
                    ip.setEndIp( end_ip );
                    //log.info("结束ip:" + list.get(1));
                    end_ip_num = StringUtil.getIpNum(list.get(1));
                    ip.setEndIpNum( end_ip_num );
                    //log.info("开始ipNum:" + end_ip_num);
                }
                if (i == 2) {
                    address = list.get(2);
                    ip.setAddress(  address );
                    if (list.get(2).equals("存真网络")
                            || list.get(2)
                            .equalsIgnoreCase("CZ88.net")) {
                        address = "";
                    }
                   // log.info("位置:" + address);
                }
                if (i == 3) {
                    specific_address = list.get(3);
                    if (specific_address.equals("存真网络")
                            || specific_address
                            .equalsIgnoreCase("CZ88.net")) {
                        specific_address = "";
                    }
                    if (specific_address != null) {
                        if (list.size() > 5) {
                            for (int j = 4; j < list.size(); j++) {
                                specific_address = specific_address
                                        + " " + list.get(j);
                            }
                        }
                        ip.setSpecificAddress( specific_address  );
                    }
                   /// log.info("具体地址:" + specific_address);
                }
            }
            lista.add( ip );
            if (lista.size() > 1000) {
                try {
                    ipMapper.addListIps( lista );
                } catch ( Exception e) {
                    e.printStackTrace();
                }
                log.info("处理成功,{} 条",count);
                lista.clear();
            }
        }
        ipMapper.addListIps( lista );
        lista.clear();
    }

    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }
}
