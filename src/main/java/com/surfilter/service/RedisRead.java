package com.surfilter.service;

import com.surfilter.dataobject.WordDO;

import java.util.Set;

public interface RedisRead {

    /**
     * 判断Redis中是否有数据
     * @param key
     * @return
     */
    boolean readRedis(String key);

    /**
     * 开始爬取数据
     * @param key
     */
    void startCrawler(String key);

    /**
     * 爬取url，获取切分词
     * @param url
     */
    Set<WordDO> getWebMessageWord(String url);

    /**
     * 爬取url，获取文本信息
     * @param url
     */
    String getWebMessageText(String url);
}
