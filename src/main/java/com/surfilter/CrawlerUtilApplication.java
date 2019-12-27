package com.surfilter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.surfilter"})
@RestController
@EnableScheduling
@MapperScan("com.surfilter.dao")
public class CrawlerUtilApplication {
    public static void main(String[] args) {
        SpringApplication.run( CrawlerUtilApplication.class, args );
    }

}
