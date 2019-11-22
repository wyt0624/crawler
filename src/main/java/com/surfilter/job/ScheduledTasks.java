package com.surfilter.job;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.surfilter.enums.Param;
import com.surfilter.service.impl.FileReadImpl;
import com.surfilter.service.impl.RedisReadImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private FileReadImpl fileRead;

    @Autowired
    private RedisReadImpl readRedis;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private StartJob startJob;

    @Autowired
    private DataSource dataSource;

    public static AtomicLong atomicLong;

    /**
     * 将数据打入Redis中
     */
    @Scheduled(fixedRate = 5000)
    public void startLoadFile(){
        logger.info("LoadFile start");
        fileRead.doMainToRedis("E:\\url_data\\");
    }

    /**
     * 读取Redis
     */
    public synchronized void ReadRedis(){
        logger.info("ReadRedis start");
        //读取Redis中是否有结果
        if (readRedis.readRedis(Param.REDIS_URL.getMsg())) {
            //开始爬取
            readRedis.startCrawler(Param.REDIS_URL.getMsg());
        } else {
            logger.debug("Redis key" + Param.REDIS_URL.getMsg() + "为空");
        }
    }
    @Scheduled(fixedRate = 10000)
    public void startCrawler() {
        if (readRedis.readRedis(Param.REDIS_URL.getMsg())) {
            String ips = redisTemplate.opsForList().leftPop(Param.REDIS_URL.getMsg(), 1L, TimeUnit.SECONDS);
            Set<String> urlSet = new Gson().fromJson(ips, new TypeToken<Set<String>>() {
            }.getType());
            startJob.connectIpServer(urlSet);
        } else {
            logger.debug("Redis key" + Param.REDIS_URL.getMsg() + "为空");
        }
    }

}