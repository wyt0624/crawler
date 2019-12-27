package com.surfilter.job;

import com.surfilter.config.BaseInfo;
import com.surfilter.content.Globle;
import com.surfilter.dao.WhoisMapper;
import com.surfilter.entity.Info;
import com.surfilter.service.impl.WhoisServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@SuppressWarnings("ALL")
@Configuration
@Slf4j
public class WhosIsJob {
    @Autowired
    BaseInfo baseInfo;
    @Autowired
    WhoisMapper whoisMapper;
    @Autowired
    WhoisServiceImpl whoisServiceImpl;

    @Scheduled(cron = "${job.param.whoisJob}")
    private void initWhois() {
        if (!baseInfo.getSysSole().equals( Globle.SYS_ROLE_WHOIS )) {
            return;
        }
        long count = 0;
        long maxId = 0;
        for (; ; ) {
            List<Info> list = whoisMapper.listWhois( maxId );
            if (list.size() <= 0) {
                break;
            }
            whoisServiceImpl.whoisRun( list );
        }
    }

}
