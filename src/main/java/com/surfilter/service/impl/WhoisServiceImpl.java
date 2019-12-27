package com.surfilter.service.impl;

import com.surfilter.config.BeanContext;
import com.surfilter.dao.WhoisMapper;
import com.surfilter.entity.Info;
import com.surfilter.util.DateTimeUtil;
import com.surfilter.whois.models.WhoisModel;
import com.surfilter.whois.utils.WhoisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("ALL")
@Service
@Slf4j
public class WhoisServiceImpl {
    @Autowired
    WhoisMapper whoisMapper;

    public void whoisRun(List<Info> list) {
        if (whoisMapper == null) {
            whoisMapper = BeanContext.getApplicationContext().getBean( WhoisMapper.class );
        }
        for (Info info : list) {
            try {
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
                log.info( "成功处理whois{} ", info.getUrl() );
            } catch (Exception e) {
                e.printStackTrace();
            }
            info.setIsWhois( 1 );
        }
        whoisMapper.updatelistWhois( list );
        log.info( "成功处理whois{}条", list.size() );
    }
}
