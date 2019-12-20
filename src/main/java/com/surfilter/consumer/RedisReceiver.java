package com.surfilter.consumer;

import com.alibaba.fastjson.JSONArray;
import com.surfilter.config.BeanContext;
import com.surfilter.config.RedisKeyInfo;
import com.surfilter.config.StartConfig;
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
    StringRedisTemplate stringRedisTemplate;
    List<String> list = new ArrayList<String>();

    public void init () {
        try {
            Thread.sleep( 8000 );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (stringRedisTemplate == null) {
            stringRedisTemplate = BeanContext.getApplicationContext().getBean(StringRedisTemplate.class);
        }
        if (redisKeyInfo == null ) {
            redisKeyInfo = BeanContext.getApplicationContext().getBean(RedisKeyInfo.class);
        }
        if (crawingService == null) {
            crawingService = BeanContext.getApplicationContext().getBean(CrawingServerImpl.class);
        }

        log.info( "消费者初始化成功" );
        for(;; ) {
            try {
                log.info( "线程总数量为{}" ,StartConfig.atomicInteger.get() );
                if (StartConfig.atomicInteger.get() > 99) {
                    try {
                        Thread.sleep( 20000 );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                StartConfig.atomicInteger.incrementAndGet();
                String message = "";
                try {
                    //message = stringRedisTemplate.opsForList().leftPop( redisKeyInfo.getCrawlerQueue() );
                    message = stringRedisTemplate.opsForList().leftPop( redisKeyInfo.getCrawlerQueue() );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (StringUtils.isNotBlank( message )) {
                    List<String> list = JSONArray.parseArray( message, String.class );
                    if (list.size() > 0) {
                        String[] strings = new String[list.size()];
                        list.toArray(strings);
                        stringRedisTemplate.opsForSet().add( redisKeyInfo.getCrawlerCache(), strings );
                        strings = null;

                        StartConfig.executorService.execute( new CrawingServerImpl( list ) );
                        //crawingService.crawingUrl( list );
                    }
                } else {
                    StartConfig.atomicInteger.decrementAndGet();
//                    if (StartConfig.atomicInteger.get() == 0){
//                        stringRedisTemplate.delete(  redisKeyInfo.getCrawlerCache() );
//                    }
                    try {
                        Thread.sleep( 20000 );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void receiveMessage(String message) {
//        List<String> list= JSONArray.parseArray(message,String.class);
//        if (list.size()>0) {
//            crawingService.crawingUrl( list );
//        }
    }
}
