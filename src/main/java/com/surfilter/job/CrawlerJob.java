package com.surfilter.job;

import com.alibaba.fastjson.JSON;
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
import java.util.List;

@SuppressWarnings("ALL")
@Component
@Slf4j
public class CrawlerJob {
    @Autowired
    RedisKeyInfo redisKeyInfo;
    @Autowired
    BaseInfo baseInfo;
    @Autowired
    UrlMapper urlMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Scheduled(cron = "${job.param.crawlerJob}")
    private void initIp() {
        if (!baseInfo.getSysSole().equals( Globle.SYS_ROLE_NOMAL)) {
            return;
        }
        long count =  stringRedisTemplate.opsForList().size( redisKeyInfo.getCrawlerQueue());
        if (stringRedisTemplate.opsForList().size( redisKeyInfo.getCrawlerQueue() ) > 0) {
            return;
        }
        if (stringRedisTemplate.hasKey(  redisKeyInfo.getCrawlerCache() )) {
            return;
        }
        urlMapper.updateListCrawlerStatus();
        List<String> listDomainUrl  = null;
        for (;;) {
            try {
                List<DomainUrl> list = urlMapper.listCrawler();
                if (list.size() == 0){
                    break;
                }
                listDomainUrl  = new ArrayList<>(  );
                for (DomainUrl domainUrl: list ){
                    if ( stringRedisTemplate.opsForSet().isMember( redisKeyInfo.getCrawlerCache(),domainUrl.getUrl() )){
                        continue;
                    }
                    listDomainUrl.add( domainUrl.getUrl() );
                }
                if (listDomainUrl.size() > 0 ) {
                    String json = JSON.toJSONString( listDomainUrl );
                    stringRedisTemplate.opsForList().rightPush( redisKeyInfo.getCrawlerQueue(), json );
                    listDomainUrl.clear();
                }
                try {
                } catch ( Exception e) {
                    e.printStackTrace();
                }
                urlMapper.updateListCrawler(list);
                log.info( "域名入爬虫队列失败的重新进入队列-成功进入队列 :{} 条" , list.size() );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
