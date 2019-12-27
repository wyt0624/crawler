package com.surfilter.job;

import com.surfilter.config.BaseInfo;
import com.surfilter.config.RedisKeyInfo;
import com.surfilter.content.Globle;
import com.surfilter.entity.Ip;
import com.surfilter.service.IIpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class FiedlityJob {
    @Autowired
    RedisKeyInfo redisKeyInfo;
    @Autowired
    BaseInfo baseInfo;
    @Autowired
    IIpService ipService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Scheduled(cron = "${job.param.fiedlityJob}")
    private void initIp() {
        if (!baseInfo.getSysSole().equals( Globle.SYS_ROLE_NOMAL )) {
            return;
        }
        log.info( "开始加载村真ip到redis" );
        int count = 0;
        for (; ; ) {
            List<Ip> list = ipService.listIps( count );
            if (list.size() > 0) {
                Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
                for (Ip ip : list) {
                    ZSetOperations.TypedTuple<String> tuple = new DefaultTypedTuple<String>( ip.getAddress() +
                            "_" + ip.getSpecificAddress(), (double) ip.getEndIpNum() );
                    tuples.add( tuple );
                }
                stringRedisTemplate.opsForZSet().add( redisKeyInfo.getFidelityIp(), tuples );
                tuples.clear();
                tuples = null;
            }
            if (list.size() < 10000) {
                count += list.size();
                log.info( "存真ip入库成功，总条数：{}", count );
                break;
            } else {
                log.info( "存真ip入库成功入缓存，数量：{}", count );
                count += 10000;
            }
        }
    }
}
