package com.surfilter.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 初始化类
 */

@Configuration
public class StartConfig {


    @PostConstruct
    public void init() {//将
        //将mysql的白名单中的信息加载到 redis

    }
}
