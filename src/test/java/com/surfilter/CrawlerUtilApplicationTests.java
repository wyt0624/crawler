package com.surfilter;

import com.surfilter.entity.WhiteUrl;
import com.surfilter.service.IWhiteListService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class CrawlerUtilApplicationTests {

    @Autowired
    IWhiteListService whiteListService;
    @Test
    void contextLoads() {
        List<WhiteUrl> list= whiteListService.listWhiteUrl();
        for (WhiteUrl wu :list){
            log.info("白名单id:{} 白名单url:{} 白名单名称:{}", wu.getId(),wu.getUrl(),wu.getName());
        }
    }

}
