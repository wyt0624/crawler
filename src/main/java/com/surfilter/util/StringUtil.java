package com.surfilter.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nmap4j.core.nmap.NMapExecutionException;
import org.nmap4j.core.nmap.NMapInitializationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 去掉字符串中的特殊字符，只保留数字，中文，英文
     * 提取中文：regEx=“[\\u4e00-\\u9fa5]";
     * 提取数字：regEx=“[0-9]";
     * 提取英文：regEx=“[a-zA-Z0-9]";
     * 提取英文和数字：regEx=“[a-zA-Z0-9]";
     * @param str
     * @return
     * @throws Exception
     */
    public static  String RemoveSymbol(String str) throws  Exception{
        String regEx="[a-zA-Z0-9\\u4e00-\\u9fa5]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            sb.append(m.group());
        }
        return new String(sb);
    }
    public static  String RemoveSymbolCh(String str) {
        String regEx="[\\u4e00-\\u9fa5]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            sb.append(m.group());
        }
        return new String(sb);
    }


    public static String filename (String url){
       return url + "_" + System.currentTimeMillis() + ".txt";
    }
    /**
     *
     * 获取手机号码
     * @param html
     * @return
     */
    public static String phoneRegEx(String html) {
        Document doc = Jsoup.parse( html );
        html="";
        StringBuilder sb = new StringBuilder("");
        for (Element ele:doc.getAllElements()) {
            sb.append( ele.text() );
        }
        String html1  =new String(sb);
        String regex="0\\d{2,3}[-]?\\d{7,8}|0\\d{2,3}\\s?\\d{7,8}|13[0-9]\\d{8}|15[1089]\\d{8}";//固定电话机手机号码。
        Pattern p   =   Pattern.compile(regex);
        Matcher m   =   p.matcher(html1);
        StringBuffer sb1 = new StringBuffer();
        while(m.find()){
            sb1.append(m.group() +",");
        }
        String  phones =  new String(sb1);
        if (phones.endsWith( "," )) {
            return phones.substring( 0,phones.length()-1 );
        } else {
            return phones;
        }
    }

    public static String qqRegEx(String html, Set<String > qqset) {
        try {
            html =  RemoveSymbol(html);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuffer qqs = new StringBuffer(  );
        for (String qq:qqset){
            int a = html.indexOf( qq );
            if (a!=-1) {
                if (a+30 <= html.length()) {
                    String subStr = html.substring( a,a + 30 );
                    String qqstr =  numRegEx(subStr);
                    if (StringUtils.isNotBlank( qqstr)) {
                        qqs.append( qqstr );
                    }
                } else {
                    String subStr = html.substring( a,html.length());
                    String qqstr =  numRegEx(subStr);
                    if (StringUtils.isNotBlank( qqstr)) {
                        qqs.append( qqstr );
                    }

                }
            }
            while (a != -1) {
                 a = html.indexOf(qq, a + 1);//*从这个索引往后开始第一个出现的位置
                if (a != -1) {
                    if (a+30 <= html.length()) {
                        String subStr = html.substring( a,a + 30 );
                        String qqstr =  numRegEx(subStr);
                        if (StringUtils.isNotBlank( qqstr)) {
                            qqs.append( qqstr );
                        }
                    } else {
                        String subStr = html.substring( a,html.length());
                        String qqstr =  numRegEx(subStr);
                        if (StringUtils.isNotBlank( qqstr)) {
                            qqs.append( qqstr );
                        }
                    }
                }
            }
        }
        String qq=  new String(qqs);
        if (qq.endsWith( "," )){
            qq = qq.substring( 0,qq.length() -1 );
        }
        html = "";
        return qq;

    }

    /**
     *        String reg = "^[\u4e00-\u9fa5]+\\s+\\d+.\\d+\\s+×\\d+\\s+\\d+.\\d+$";
     * @param subStr
     * @return
     */
    private static String numRegEx(String subStr) {
        String regEx = "[0-9]+";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(subStr);
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            if (m.group().length()>=5 && m.group().length()<=15 ) {
                sb.append( m.group() + ",");
            }
        }
        String qq = new String(sb);

        return qq;
    }
    public static String  addressRegEx(String html){
        String regex="(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
        Pattern p   =   Pattern.compile(regex);
        Matcher m   =   p.matcher(html);
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            sb.append(m.group());
        }
        String address = new String(sb);
        if (StringUtils.isNotBlank( address )){
            return address;
        }
        return null;
    }

    public static void main(String[] args) throws NMapExecutionException, NMapInitializationException {
      //  WhoisModel wm = WhoisUtil.queryWhois( "baidu.com" );
//        String ip = "";
//        try{
////            InetAddress[] inetAdresses = InetAddress.getAllByName("baidu.com");
////            if(inetAdresses != null && inetAdresses.length > 0){
////                ip = inetAdresses[0].getHostAddress();
////            }
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        System.out.println(ip);
//        AtomicInteger atomicInteger = new AtomicInteger(0);
//        System.out.println(atomicInteger.get());
//        atomicInteger.incrementAndGet();
//        System.out.println(atomicInteger.get());
//        atomicInteger.decrementAndGet();
//        System.out.println(atomicInteger.get());
//        System.out.println(getIpNum("124.156.98.105"));
          List<String > list = new ArrayList<String>();
          list.add("aaa");
          list.add("bbb");
          list.add("ccc");
          list.add("ccc");
          list.add("ccc");
          list.add("ccc");
          list.add("ccc");
        String[] strings = new String[list.size()];
        list.toArray(strings);




          getChangeCan("test",strings);
    }
    /**
     * 调用nmap进行扫描
     * @param nmapDir nmap路径
     * @param command 执行命令
     *
     * @return 执行回显
     * */
    public static  String getReturnData(String nmapDir,String command){
        Process process = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            process = Runtime.getRuntime().exec(nmapDir + " " + command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
            String line = null;
            while((line = reader.readLine()) != null){
                stringBuffer.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }
    public static  long getIpNum (String ip) {
        long ipnum = 0;
        try {
            String[] ips = ip.split( "\\." );
            for ( int i = 0 ; i<ips.length; i++ ) {
                if (i == 0) {
                    ipnum +=  Long.parseLong( ips[0] ) * 255 * 255 * 255;
                }
                if (i == 1) {
                    ipnum +=  Long.parseLong( ips[1] ) * 255 * 255;
                }
                if (i == 2) {
                    ipnum +=  Long.parseLong( ips[2] ) * 255;
                }
                if (i == 3) {
                    ipnum +=  Long.parseLong( ips[3] );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipnum;
    }


    public static void  getChangeCan(String aa,Object... var2){
        System.out.println(var2);
        for (Object aaa :var2) {
            System.out.println(aaa);
        }
    }
}
