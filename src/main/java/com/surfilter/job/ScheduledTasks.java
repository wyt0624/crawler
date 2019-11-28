package com.surfilter.job;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.surfilter.enums.Param;
import com.surfilter.service.impl.FileReadImpl;
import com.surfilter.service.impl.RedisReadImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    @Value("${job.param.read-redis-path}")
    private String readToRedisPath;

    @Value("${job.param.write-file-path}")
    private String writeFilePath;

    @Value("${job.param.yes-file-path}")
    private String writeYesFilePath;

    public static AtomicLong atomicLong = new AtomicLong(0);

    /**
     * 将数据打入Redis中
     */
   /* @Scheduled(fixedRate = 5000)*/
    public void startLoadFile(){
        logger.info("LoadFile start");
        fileRead.doMainToRedis(readToRedisPath);
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
        if (readRedis.readRedis(Param.REDIS_URL.getMsg()) && atomicLong.get() < 50) {
            String ips = redisTemplate.opsForList().leftPop(Param.REDIS_URL.getMsg(), 1L, TimeUnit.SECONDS);
            Set<String> urlSet = new Gson().fromJson(ips, new TypeToken<Set<String>>() {
            }.getType());
            atomicLong.set(urlSet.size());
            startJob.startCrawlerServer(urlSet);
        } else {
            logger.debug("Redis key" + Param.REDIS_URL.getMsg() + "为空");
        }
    }

}