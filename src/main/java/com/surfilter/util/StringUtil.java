package com.surfilter.util;

import com.surfilter.entity.Info;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
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
    public static  String RemoveSymbolNumStr(String str) throws  Exception{
        String regEx="[a-zA-Z0-9]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            sb.append(m.group());
        }
        return new String(sb);
    }
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
    public static  String RemoveSymbolNum(String str) throws  Exception{
        String regEx="[0-9]";
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
    /**
     * 匹配qq
     * */
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

    /**
     * 判断字符串中是否包含 除了 英文 数字 和. 之外的字符。
     * @param str
     * @return
     */
    public static boolean RemoveSymbolnomal(String str) {
        String regEx="[a-zA-Z0-9.]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(m.find()) {
            sb.append( m.group() );
        }
        String ss =  new String(sb);
        boolean flag = true;
        if (ss.length() == str.length()) {
            flag =  true;
        } else {
            flag = false;
        }
        ss = null;
        return flag;
    }


    public static  Long  getIpNum (String ip) {
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

    public static boolean domainClean(String str) {
        if (StringUtils.isEmpty( str )) {
            return false;
        }
        if (str.length() <3 || str.length() >400) {
            return false;
        }
        if (str.endsWith( "*" )) {
            return false;
        }
        if (str.startsWith( "-" )) {
            return false;
        }
        return true;

    }


    private static int digitLetterCount(String url) {
        int num = 0;
        try {
            String str =RemoveSymbolNumStr(url);
            char [] ccs = str.toCharArray();
            int len  = 0;
            int swip = 0; //0 是数字。 1 是字母。
            for (char cc: ccs) {
                int  digit = Integer.valueOf( cc );
                if (len == 0 ) {
                    if ( digit >= 48 && digit<= 57) {
                        swip = 0;
                    } else  if (digit >= 65 && digit<= 90) {
                        swip = 1;
                    } else  if (digit >= 97 && digit<= 122) {
                        swip = 1;
                    }
                    len ++;
                    continue;
                }
                if ( digit >= 48 && digit<= 57) {
                    if (swip == 1) {
                        swip = 0;
                        num ++;
                    }
                } else  if (digit >= 65 && digit<= 90) {
                    if (swip == 0) {
                        swip = 1;
                        num ++;
                    }
                } else  if (digit >= 97 && digit<= 122) {
                    if (swip == 0) {
                        swip = 1;
                        num ++;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    private static double maxSubLength(String url) {
        double num = 0;
        String [] digit = url.split( "\\." );
        for (String str : digit) {
            double nule = new BigDecimal( (float) str.length() / url.length() ).setScale( 2, BigDecimal.ROUND_HALF_UP ).doubleValue();
            if (num < nule) {
                num = nule;
            }
        }
        return num;

    }

    private static int maxDigitLength(String url) {
        int num = 0;
        String [] digit = url.split( "\\." );
        for (String str : digit) {
            try {
                int numlen  =  RemoveSymbolNum(url).length();
                if (num < numlen) {
                    num = numlen;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return num;

    }

    //数字占域名的百分比。
    private static double digitLengthPercent(String url) {
        double dlp = 0;

        int lenNum = 0 ;
        try {
            lenNum =  RemoveSymbolNum(url).length();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (lenNum ==0 ) {
            return dlp;
        } else {
            dlp = new BigDecimal( (float) lenNum / url.length() ).setScale( 2, BigDecimal.ROUND_HALF_UP ).doubleValue();
        }
        return dlp;
    }


    public static int getMaches(String str,String substr){
        int count = 0;//count用来接收子字符串substr在字符串str中出现的次数
        int i = 0;
        while(str.indexOf(substr,i) != -1) {
            count++;
            i=str.indexOf(substr, i)+substr.length();
        }
        return count;
    }


    public static void nmapOfPort(Info info, int operatingSystemType) {
        //判断是什么操作系统。。 1 是linux 系统 2 是windows 系统 0 是初始化为确定。
        String str = null;
        if ( operatingSystemType == 0  ) {
            String os = System.getProperty("os.name");
            if(os.toLowerCase().startsWith("win")){
                operatingSystemType = 2;
            } else {
                operatingSystemType = 1;
            }
        }
        if (operatingSystemType == 1){
            str =  nmapInfo(info.getUrl(),"nmap");

        } else if  (operatingSystemType == 2) {
            str=  nmapInfo(info.getUrl(),"D:/nmap-6.46/nmap");
        }
        if (StringUtils.isNotBlank( str )) {
            info.setPort( str );
        }
    }

    public static String nmapInfo(String url,String dirPath) {
        String str = null;
        try {
           //str  = getReturnData( "", "D:/nmap-6.46/nmap  -PS -n -F --host-timeout 10s " + url);
           str  = getReturnData( "", dirPath + " -PS -n -F --host-timeout 10s " + url);
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return str;
    }



    /**
     * 调用nmap进行扫描
     * @param nmapDir nmap路径
     * @param command 执行命令
     *
     * @return 执行回显
     * */
    public static  String getReturnData(String nmapDir,String command) throws  Exception{
        Process process = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            process = Runtime.getRuntime().exec(nmapDir + " " + command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
            String line = null;
            boolean flag = false;
            while((line = reader.readLine()) != null) {
                if (line.startsWith( "PORT" )) {
                    flag = true;
                    continue;
                }
                if (line.startsWith( "Nmap" )) {
                    flag = false;
                }
                if ( flag ) {
                    if (StringUtils.isNotBlank(line)) {
                        stringBuffer.append( line.trim().replaceAll( " +", " " ) + "|" );
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str = stringBuffer.toString();
        if (str.endsWith( "|" )) {
            str = str.substring( 0,str.length()-1 );
        }
        if (StringUtils.isNotBlank( str )) {
            if (str.contains( "unknown" )) {
                str = str.replace( "unknown","" );
            }
        }
        if (str.length() > 4000) {
            return str.substring( 0,4000 );
        }

        return str;
    }
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
    public static  String RemoveSymbolaaa(String str) throws  Exception{
        String regEx="[0-9]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            sb.append(m.group() + ",");
        }
        return new String(sb);
    }
    //符合数据规范的 在入队列。
    public static void domainExClean(String url, Info info) {
        StringBuffer rule = new StringBuffer( "" );
        int count = 0;
        // 域名长度在5 - 20
        if(url.length() > 4 && url.length() <= 20) {
            count ++;
            rule.append( "1," );
        }
        //  . 出现在1 - 4 之间。
        int num  = getMaches(url,".");
        if (num > 0 && num <4) {
            count ++;
            rule.append( "2," );
        }
        //域名中特殊字符频率中。黄色网站及域名网站没有变化。故 count+1
        count ++;
        rule.append( "3," );
        // 数字 占域名总长度。 0 - 0.8 之间。
        double digit_length_percent = digitLengthPercent(url);
        if (digit_length_percent >=0  && digit_length_percent< 0.8) {
            count ++;
            rule.append( "4," );
        }
        //第五个是分隔符内数字个数的最大值, 它与上一项的主要差别在于与总长度无关, 同样的, 对正常域名来说, 很少出现大于2个的数字, 而赌博色情域名则较长出现多个数字
        int  max_digit_length  = maxDigitLength(url);
        if (max_digit_length >=0 && max_digit_length < 10) {
            count ++;
            rule.append( "5," );
        }
        // 第六个是分隔符间的最大长度, 结果与域名总长度类似 类似  长度超过 %70
        double max_sub_length = maxSubLength(url);
        if (max_sub_length >= 0.7) {
            count ++;
            rule.append( "6," );
        }
        //第七个是数字字母的转换频率, 如a11b的转换频率就是2, 这一项正常域名和赌博色情域名的差别也比较大, 正常域名的切换频率普遍都比较小,而赌博色情域名则大多有1-3次的转换频率
        int digit_letter_count = digitLetterCount(url);
        if (digit_letter_count >= 1 && digit_letter_count <= 5) {
            count ++;
            rule.append( "7" );
        }
        String rules = new String(rule);
        if (rules.endsWith( "," )) {
            info.setRule( rules.substring( 0,rules.length()-1 ) );
        } else {
            info.setRule( rules );
        }
        rules = null;
        rule = null;
        info.setRuleCount( count );
    }
    public static void main(String[] args) {
//        String str=  "80/tcp    open  http 443/tcp   open  https 8888/tcp  open  sun-answerbook 49152/tcp open   49153/tcp open   49154/tcp open   49155/tcp open   49156/tcp open   49157/tcp open";
//        //RemoveSymbolaaa(str);
//
//
////        System.out.println(str.length());
////        System.out.println(str);
////        HttpUtil.getDocByUrl("http://www.chilangedu.com");
        domainExClean("nsg-dcloud.pingan.com.cn", new Info());
    }
}
