package com.surfilter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.surfilter.config.RedisKeyInfo;
import com.surfilter.dao.CountryMapper;
import com.surfilter.dao.IpMapper;
import com.surfilter.dao.WhiteUrlMapper;
import com.surfilter.entity.CountryInfo;
import com.surfilter.entity.Phishing;
import com.surfilter.entity.WhiteUrl;
import com.surfilter.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    StringRedisTemplate stringRedisTemplate;

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
//        InputStreamReader isr = null;
//        BufferedReader br = null;
//        File file = new File("D:/ttt1.txt");
//        isr = new InputStreamReader(new FileInputStream(file), "utf-8");
//        br = new BufferedReader(isr);
//        String str;
//        // 按行读取字符串
//        int count = 0 ;
//        boolean flag = true;
//        List<Ip> lista  = new ArrayList<Ip>(  );
//
//        while ((str = br.readLine()) != null) {// 使用readLine方法，一次读一行
//            count ++;
//            String start_ip = "";
//            long strat_ip_num = 0;
//            String end_ip = "";
//            long end_ip_num = 0;
//            String prince = "";
//            String address = "";
//            String specific_address = "";
//            // 带有 IANA机构, IANA
//            if (str.contains("IP数据库共有数据") || str.equals("")) {
//                continue;
//            }
//            List<String> list = new ArrayList<String>();
//            String[] strs = str.split(" ");
//            for (int i = 0; i < strs.length; i++) {
//                if (!strs[i].equals("")) {
//                    list.add(strs[i]);
//                }
//            }
//            Ip ip = new Ip();
//            for (int i = 0; i < list.size(); i++) {
//                if (i == 0) {
//                    start_ip = list.get(0);
//                    ip.setStartIp( start_ip );
//                   // log.info("开始ip:" + list.get(0));
//                    strat_ip_num = StringUtil.getIpNum(list.get(0));
//                    ip.setStartIpNum( strat_ip_num );
//                    //log.info("结束ipNum:" + strat_ip_num);
//                }
//                if (i == 1) {
//                    end_ip = list.get(1);
//                    ip.setEndIp( end_ip );
//                    //log.info("结束ip:" + list.get(1));
//                    end_ip_num = getIpNum(list.get(1));
//                    ip.setEndIpNum( end_ip_num );
//                    //log.info("开始ipNum:" + end_ip_num);
//                }
//                if (i == 2) {
//                    address = list.get(2);
//                    ip.setAddress(  address );
//                    if (list.get(2).equals("存真网络")
//                            || list.get(2)
//                            .equalsIgnoreCase("CZ88.net")) {
//                        address = "";
//                    }
//                   // log.info("位置:" + address);
//                }
//                if (i == 3) {
//                    specific_address = list.get(3);
//                    if (specific_address.equals("存真网络")
//                            || specific_address
//                            .equalsIgnoreCase("CZ88.net")) {
//                        specific_address = "";
//                    }
//                    if (specific_address != null) {
//                        if (list.size() > 5) {
//                            for (int j = 4; j < list.size(); j++) {
//                                specific_address = specific_address
//                                        + " " + list.get(j);
//                            }
//                        }
//                        ip.setSpecificAddress( specific_address  );
//                    }
//                   /// log.info("具体地址:" + specific_address);
//                }
//            }
//            lista.add( ip );
//            if (lista.size() > 1000) {
//                try {
//                    ipMapper.addListIps( lista );
//                } catch ( Exception e) {
//                    e.printStackTrace();
//                }
//                log.info("处理成功,{} 条",count);
//                lista.clear();
//            }
//        }
//        ipMapper.addListIps( lista );
//        lista.clear();
    }

}
