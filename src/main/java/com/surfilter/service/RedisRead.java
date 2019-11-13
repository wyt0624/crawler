package com.surfilter.service;

import com.surfilter.dataobject.WordDO;

import java.util.Set;

public interface RedisRead {

    public boolean readRedis(String key);

    public void startCrawler(String key);

    public Set<WordDO> getWebMessageText(String url);
}
