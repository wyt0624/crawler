package com.surfilter.job;

import com.surfilter.config.BaseInfo;
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
    @Autowired
    @Scheduled(cron = "${job.param.readFile}")
    private void readFile() {
        //读取文件中的内容。并将文件放到redis中去重。然后插入数据库 然后插入队列。
        fileRead.doMainToRedis();
    }
}