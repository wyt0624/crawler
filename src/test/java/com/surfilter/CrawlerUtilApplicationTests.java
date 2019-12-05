package com.surfilter;

import com.surfilter.config.RedisKeyInfo;
import com.surfilter.service.IWhiteListService;
import com.surfilter.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

@SpringBootTest
@Slf4j
class CrawlerUtilApplicationTests {

    @Autowired
    IWhiteListService whiteListService;
    @Autowired
    RedisKeyInfo redisKeyInfo;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Test
    void contextLoads() {

    }

}
