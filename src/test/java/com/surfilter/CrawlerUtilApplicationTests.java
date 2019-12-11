package com.surfilter;

import com.surfilter.dao.IpMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SuppressWarnings("ALL")
@SpringBootTest
@Slf4j
class CrawlerUtilApplicationTests {

   @Autowired
    IpMapper ipMapper;
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
