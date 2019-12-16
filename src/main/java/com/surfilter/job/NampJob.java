package com.surfilter.job;

import com.surfilter.config.BaseInfo;
import com.surfilter.content.Globle;
import com.surfilter.entity.Info;
import com.surfilter.service.INmapService;
import com.surfilter.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@SuppressWarnings("ALL")
@Configuration
@Slf4j
public class NampJob {
    public static int operatingSystemType = 0; //0 是初始化状态。  1 是linux  2 是  windos
    @Autowired
    BaseInfo baseInfo;
    @Autowired
    INmapService nmapService;
    @Scheduled(cron = "${job.param.whoisJob}")
    private void initWhois() {
        if (!baseInfo.getSysSole().equals( Globle.SYS_ROLE_NMAP)) {
            return;
        }
        long count = 0;
        for (;;) {
            List<Info> list = nmapService.listNmap();
            if (list.size() <= 0 ) {
                log.info( "没有要执行的nmap数据" );
                break;
            }

            for (Info  info: list)  {
                try {
                    StringUtil.nmapOfPort( info, operatingSystemType );
                    log.info( "nmap 域名:{},的结果为:{}", info.getUrl(), info.getPort() );
                    info.setIsPort( 1 );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //修改数据库状态。
            try {
                nmapService.updateListNmap( list );
                count  += list.size();
                log.info( "执行nmap端口查询:{} 条" ,count );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
