package com.surfilter.job;

import com.surfilter.config.BaseInfo;
import com.surfilter.config.RedisKeyInfo;
import com.surfilter.content.Globle;
import com.surfilter.dao.UrlMapper;
import com.surfilter.entity.DomainUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("ALL")
@Component
@Slf4j
public class DomainUrlJob {
    @Autowired
    RedisKeyInfo redisKeyInfo;
    @Autowired
    BaseInfo baseInfo;
    @Autowired
    UrlMapper urlMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Scheduled(cron = "${job.param.domainUrlJob}")
    private void initIp() {
        if (baseInfo.getSysSole().equals( Globle.SYS_ROLE_CONSUMER)) {
            return;
        }
        List<String> listDomainUrl  = null;
        for (;;) {
            try {
                List<DomainUrl> list = urlMapper.listCache();
                if (list.size() == 0){
                    break;
                }
                listDomainUrl  = new ArrayList<>(  );
                Iterator<DomainUrl> it =   list.iterator();
                while (it.hasNext()) {
                    DomainUrl domainUrl = it.next();
                    if (!stringRedisTemplate.opsForSet().isMember( redisKeyInfo.getDomainUrl(), domainUrl.getUrl() )) {
                        try {
                            stringRedisTemplate.opsForSet().add( redisKeyInfo.getDomainUrl(), domainUrl.getUrl() );
                        } catch (Exception e) {
                            it.remove();
                            e.printStackTrace();
                        }
                    }
                }
                try {
                } catch ( Exception e) {
                    e.printStackTrace();
                }
                if (list.size()> 0) {
                    urlMapper.updateListCache( list );
                }
                log.info( "域名入爬虫队列失败的重新进入队列-成功进入队列 :{}" + list.size() );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
