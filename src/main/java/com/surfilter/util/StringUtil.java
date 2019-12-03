package com.surfilter.util;

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
        String regex="0\\d{2,3}[-]?\\d{7,8}|0\\d{2,3}\\s?\\d{7,8}|13[0-9]\\d{8}|15[1089]\\d{8}";//固定电话机手机号码。
        Pattern p   =   Pattern.compile(regex);
        Matcher m   =   p.matcher(html);
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            sb.append(m.group() +",");
        }
        String  phones =  new String(sb);
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
                String subStr = html.substring( a,a + 30 );
                String qqstr =  numRegEx(subStr);
            }
            System.out.println(a);
            while (a != -1) {
                a = html.indexOf(qq, a + 1);//*从这个索引往后开始第一个出现的位置
                if ((a + 30)< html.length()) {
                    String subStr = html.substring( a, a + 30 );
                    String qqstr = numRegEx(subStr);
                }

            }
        }
        return new String(qqs);

    }

    private static String numRegEx(String subStr) {
        String regEx = "^.\\d{5,15}$";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(subStr);
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            System.out.println(m.group());
        }
        return null;
    }


    public static String  addressRegEx(String html){
        String regex="(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
        Matcher m=Pattern.compile(regex).matcher(html);
        return  null;
    }

    public static void main(String[] args) {
        String html = "aaaawef540766024";

        numRegEx(html);
    }



}
