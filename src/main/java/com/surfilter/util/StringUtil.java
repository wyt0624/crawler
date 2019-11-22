package com.surfilter.util;

import com.surfilter.MyApplicationRunner;
import com.surfilter.dataobject.UrlDO;

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
        String regEx="[\\u4e00-\\u9fa5]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            sb.append(m.group());
        }
        return new String(sb);
    }

    public static boolean isWhiteUrl(String url) {
        boolean flag = false;
        if (!MyApplicationRunner.whiteUrlList.isEmpty()) {
            for (UrlDO urlDO : MyApplicationRunner.whiteUrlList) {
                if (urlDO.getUrl().contains(url)) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        }
        return flag;
    }

}
