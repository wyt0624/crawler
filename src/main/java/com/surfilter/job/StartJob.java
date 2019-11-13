package com.surfilter.job;


import com.surfilter.enums.Param;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class StartJob {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(1000);

    public void connectIpServer(Set<String> urlList){
        //遍历每个程序
        for(String url : urlList){
            //启动线程
            String key = Param.REDIS_URL.getMsg();
            executorService.execute(new CrawlerUrl(url, key));
        }
    }
}
