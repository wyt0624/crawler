package com.surfilter.job;

import com.surfilter.config.BaseInfo;
import com.surfilter.content.Globle;
import com.surfilter.entity.Info;
import com.surfilter.service.IWhoisService;
import com.surfilter.util.DateTimeUtil;
import com.surfilter.whois.models.WhoisModel;
import com.surfilter.whois.utils.WhoisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@Slf4j
public class WhosIsJob {
    @Autowired
    BaseInfo baseInfo;
    @Autowired
    IWhoisService whoisService;
    @Scheduled(cron = "${job.param.whoisJob}")
    private void initWhois() {
        if (!baseInfo.getSysSole().equals( Globle.SYS_ROLE_WHOIS)) {
            return;
        }
        long count = 0;
        for (;;) {
            List<Info> list = whoisService.listWhois();
            if (list.size() <= 0 ) {
                log.info("whois 执行完成 {} 条" ,count );
                break;
            }
            for (Info info:list) {
                WhoisModel wm = WhoisUtil.queryWhois( info.getUrl() );
                if (wm != null) {
                    if (DateTimeUtil.dateToTimstamp( wm.getCtime() ) != null && wm.getCtime() > 100000) {
                        info.setCreationTime( DateTimeUtil.dateToTimstamp( wm.getCtime() ) );
                    }
                    if (DateTimeUtil.dateToTimstamp( wm.getEtime() ) != null && wm.getCtime() > 100000) {
                        info.setExpireTime( DateTimeUtil.dateToTimstamp( wm.getEtime() ) );
                    }
                    if (StringUtils.isNotBlank( wm.getPhone() )) {
                        info.setTel( wm.getPhone() );
                    }
                    if (StringUtils.isNotBlank( wm.getEmail() )) {
                        info.setEmail( wm.getEmail() );
                    }
                    if (DateTimeUtil.dateToTimstamp( wm.getUtime() ) != null && wm.getCtime() > 100000) {
                        info.setLastUpdateTime( DateTimeUtil.dateToTimstamp( wm.getUtime() ) );
                    }
                }
                info.setIsWhois( 1 );
            }
            try {
                whoisService.updateWhoisInfo( list );
                count += list.size();
                log.info("whois处理{}条",count);
                list.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
