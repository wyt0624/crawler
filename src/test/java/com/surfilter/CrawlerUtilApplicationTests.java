package com.surfilter;

import com.alibaba.fastjson.JSON;
import com.surfilter.config.RedisKeyInfo;
import com.surfilter.service.IWhiteListService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;

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
      String domainUrl = "baidu.com.com.cn";
      //stringRedisTemplate.convertAndSend( redisKeyInfo.getCrawlerQueue(),domainUrl);
        List<String> list = new ArrayList<String>();
        list.add(domainUrl);
        String json = JSON.toJSONString(list);
        stringRedisTemplate.convertAndSend(redisKeyInfo.getCrawlerQueue(),json);

    }

}
