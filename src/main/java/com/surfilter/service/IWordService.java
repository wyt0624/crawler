package com.surfilter.service;

import java.util.Map;

public interface IWordService {
    /***
     * 获取分词
     * @param url
     * @return 返回分词集合
     */
    Map<String,Object> getWords(String url) throws Exception;
}
