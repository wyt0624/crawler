package com.surfilter.config;

import com.surfilter.content.Globle;
import org.apache.commons.net.whois.WhoisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class WhoisStart {
    @Autowired
    BaseInfo baseInfo;
    public static Map<WhoisClient, Long> whoisMap = new ConcurrentHashMap<WhoisClient, Long>();

    @PostConstruct
    public void init() {//将白名单放到 redis中。
        if (!baseInfo.getSysSole().equals( Globle.SYS_ROLE_WHOIS )) {
        }
        Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    Iterator<Map.Entry<WhoisClient, Long>> it = WhoisStart.whoisMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<WhoisClient, Long> entry = it.next();
                        Long value = entry.getValue();
                        if ((System.currentTimeMillis() - value) > 5000) {
                            WhoisClient wc = entry.getKey();
                            try {
                                if (wc != null) {
                                    wc.disconnect();
                                }
                                it.remove();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        Thread.sleep( 10000 );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } );
        thread.start();

    }
}
