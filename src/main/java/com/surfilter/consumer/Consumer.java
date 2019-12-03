package com.surfilter.consumer;

import com.surfilter.config.ApplicationContextProvider;
import com.surfilter.config.RedisKeyInfo;
import com.surfilter.service.ICrawingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class Consumer extends  Thread{
    @Autowired
    ICrawingService crawingService;
    @Autowired
    RedisKeyInfo redisKeyInfo;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public void run() {
        if (stringRedisTemplate == null){
            this.stringRedisTemplate = ApplicationContextProvider.getBean(StringRedisTemplate.class);
        }
        if (crawingService == null){
            this.crawingService = ApplicationContextProvider.getBean( ICrawingService.class);
        }
        if (redisKeyInfo == null){
            this.redisKeyInfo = ApplicationContextProvider.getBean(RedisKeyInfo.class);
        }
        for (;;) {
            try {
                String url = stringRedisTemplate.opsForList().leftPop(redisKeyInfo.getCrawlerQueue());
               if (StringUtils.isNotBlank(url)){
                   crawingService.crawingUrl(url);
               } else {
                   url = null;//
               }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
