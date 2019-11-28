package com.surfilter.config;

import com.surfilter.entity.WhiteUrl;
import com.surfilter.service.IWhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化类
 */

@Configuration
public class StartConfig {
    @Autowired
    IWhiteListService whiteListService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisKeyInfo redisKeyInfo;

    @PostConstruct
    public void init() {//将
        stringRedisTemplate.opsForHash().delete( redisKeyInfo.getWhile_url() );
        List<WhiteUrl> list = whiteListService.listWhiteUrl();
        Map<String,String> data = new HashMap<>();
        int count = 0 ;
        for (WhiteUrl wu :list) {
            data.put( wu.getUrl(),wu.getName() );
            count ++ ;
            if ( count % 2000 == 0 ){
                stringRedisTemplate.opsForHash().putAll(redisKeyInfo.getWhile_url(),data);
                data.clear();
                count = 0;
            }
        }
        stringRedisTemplate.opsForHash().putAll(redisKeyInfo.getWhile_url(),data);
        data.clear();
    }
}
