package com.surfilter.service.impl;

import com.alibaba.fastjson.JSON;
import com.surfilter.config.BaseInfo;
import com.surfilter.config.RedisKeyInfo;
import com.surfilter.dao.UrlMapper;
import com.surfilter.entity.DomainUrl;
import com.surfilter.service.FileRead;
import com.surfilter.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
@Service
@Slf4j
public class FileReadImpl implements FileRead {
    @Autowired
    BaseInfo baseInfo;
    @Autowired
    RedisKeyInfo redisKeyInfo;
    @Autowired
    UrlMapper urlMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * // 如入缓存， 入队列， 入mysql
     */
    public void doMainToRedis() {
        File file = new File( baseInfo.getUrlReadPath() );
        File[] files = file.listFiles( new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return !name.endsWith( ".bak" );
            }
        } );
        List<DomainUrl> listUrl = new ArrayList<DomainUrl>();
        try {
            for (File tempFile : files) {
                log.info( "开始入库文件:{}", tempFile.getName() );
                FileReader fr = new FileReader( tempFile.getPath() );
                BufferedReader bf = new BufferedReader( fr );
                String str;
                // 按行读取字符串
                int count = 0;
                int cacheStatus = 0;
                int crawlingStatus = 0;
                boolean flag = true;
                List<String> list = new ArrayList<>();
                while ((str = bf.readLine()) != null) {
                    boolean isNomal = StringUtil.domainClean( str );
                    if (!isNomal) {
                        continue;
                    }
                    str = str.replace( "\"", "" ).trim();
                    //白名单判断。
                    if (stringRedisTemplate.opsForHash().hasKey( redisKeyInfo.getWhileUrl() ,str ) ){  //判断是否在白名单中。
                        continue;
                    }
                    if (!stringRedisTemplate.opsForSet().isMember( redisKeyInfo.getDomainUrl(), str )) {
                        try {
                            stringRedisTemplate.opsForSet().add( redisKeyInfo.getDomainUrl(), str );
                            cacheStatus = 1;
                        } catch (Exception e) {
                            cacheStatus = 2;
                            e.printStackTrace();
                        }
                        try {// 是否已经发送爬取数据的队列。  如果已经发送了就 直接修改为1 如果发送失败了就改为2
                            list.add( str );
                            crawlingStatus = 1;
                        } catch (Exception e) {
                            crawlingStatus = 2;
                            e.printStackTrace();
                        }
                    } else {
                        continue;
                    }
                    DomainUrl du = new DomainUrl();
                    du.setCacheStatus( cacheStatus );
                    du.setCrawlingStatus( crawlingStatus );
                    du.setOperateTime( new Timestamp( System.currentTimeMillis() ) );
                    du.setUrl( str );
                    listUrl.add( du );
                    count++;
                    if (listUrl.size() > 50) {
                        try {
                            String json = JSON.toJSONString( list );
                            stringRedisTemplate.opsForList().rightPush( redisKeyInfo.getCrawlerQueue(), json );
                            if (listUrl.size() > 0) {
                                urlMapper.addDomainUrl( listUrl );
                            }
                            list.clear();
                            log.info( "{} 入库 {} 条结果", tempFile.getName(), count );
                        } catch (Exception e) {
                            e.printStackTrace();
                            flag = false;// 如果有出错 就需要重新 加载文件。。不能讲 文件改成.bak
                        }
                        listUrl.clear();
                    }
                }
                try {
                    String json = JSON.toJSONString( list );
                    stringRedisTemplate.opsForList().rightPush( redisKeyInfo.getCrawlerQueue(), json );
                    if (listUrl.size() > 0) {
                        urlMapper.addDomainUrl( listUrl );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    flag = false;// 如果有出错 就需要重新 加载文件。。不能讲 文件改成.bak
                }
                listUrl.clear();
                bf.close();
                fr.close();
                if (flag) {
                    tempFile.renameTo( new File( tempFile.getPath() + ".bak" ) );
                    log.info( "{}  入库完成，总结果{}", tempFile.getName(), count );
                } else {
                    log.info( "{}  入库完成，入mysql有异常，重新录入", tempFile.getName() );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
