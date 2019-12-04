package com.surfilter.consumer;

import com.surfilter.config.ApplicationContextProvider;
import com.surfilter.config.RedisKeyInfo;
import com.surfilter.service.impl.CrawingServerImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Consumer extends  Thread{
    @Autowired
    CrawingServerImpl crawingService;
    @Autowired
    RedisKeyInfo redisKeyInfo;
    @Autowired
    CrawingServerImpl crawingServerImpl;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public void run() {
        try {
            if (stringRedisTemplate == null) {
                this.stringRedisTemplate = ApplicationContextProvider.getBean( StringRedisTemplate.class );
            }
            if (redisKeyInfo == null) {
                this.redisKeyInfo = ApplicationContextProvider.getBean( RedisKeyInfo.class );
            }
            if (crawingServerImpl == null) {
                this.crawingServerImpl = ApplicationContextProvider.getBean( CrawingServerImpl.class );
            }
            for (;;) {
                try {
                    String url = stringRedisTemplate.opsForList().leftPop( redisKeyInfo.getCrawlerQueue() );
                    // 判断是否是白名单。
                    if (StringUtils.isNotBlank( url )) {
                        boolean white = stringRedisTemplate.opsForHash().hasKey( redisKeyInfo.getWhileUrl(), url );
                        if (!white) {
                            crawingServerImpl.crawingUrl( url );
                        } else {
                            log.info( "{}，在白名单中，不进行处理", url );
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
