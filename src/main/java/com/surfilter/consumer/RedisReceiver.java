package com.surfilter.consumer;

import com.surfilter.config.RedisKeyInfo;
import com.surfilter.service.impl.CrawingServerImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RedisReceiver {
    @Autowired
    CrawingServerImpl crawingService;
    @Autowired
    RedisKeyInfo redisKeyInfo;
    @Autowired
    CrawingServerImpl crawingServerImpl;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    List<String> list = new ArrayList<String>();
    public void receiveMessage(String message) {
        if (StringUtils.isNotBlank(message)) {
            boolean white = stringRedisTemplate.opsForHash().hasKey( redisKeyInfo.getWhileUrl(), message );
            if (!white) {
                list.add( message );
                if (list.size()> 200) {
                    crawingServerImpl.crawingUrl( list );
                    list = new ArrayList<>( );
                }
            }
        }
    }
}
