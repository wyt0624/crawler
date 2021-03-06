package com.surfilter.job;

import com.surfilter.config.BaseInfo;
import com.surfilter.content.Globle;
import com.surfilter.service.FileRead;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReadFileJob {
    @Autowired
    BaseInfo baseInfo;
    @Autowired
    FileRead fileRead;

    @Scheduled(fixedRate = 1000 * 60, initialDelay = 10000)
    private void readFile() throws InterruptedException {
        if (!baseInfo.getSysSole().equals( Globle.SYS_ROLE_NOMAL )) {
            return;
        }
        log.info( "读取文件程序启动" );
        //读取文件中的内容。并将文件放到redis中去重。然后插入数据库 然后插入队列。
        fileRead.doMainToRedis();
    }
}
