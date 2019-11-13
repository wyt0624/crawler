package com.surfilter.job;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.surfilter.MyApplicationRunner;
import com.surfilter.dataobject.WordDO;
import com.surfilter.service.RedisRead;
import com.surfilter.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

@Slf4j
public class CrawlerUrl implements Runnable {

    private String url ;
    private String key ;

    public CrawlerUrl(String url,String key) {
        this.url = url;
        this.key = key;
    }


    private RedisRead redisRead;

    private StringRedisTemplate redisTemplate;


    @Override
    public void run() {
        redisTemplate = ApplicationContextUtil.getBean(StringRedisTemplate.class);
        redisRead = ApplicationContextUtil.getBean(RedisRead.class);
        Set<WordDO> set = redisRead.getWebMessageText(url);
        BufferedWriter bw = null;
        if (!set.isEmpty()) {
            File file = new File("E://word_data//" + url.replaceAll("\"","") + "_" + System.currentTimeMillis());
            try {
                bw = new BufferedWriter(new FileWriter(file));
                if(!file.exists()) {
                    file.createNewFile();
                }
                for (WordDO wordDO : set) {
                    bw.write(wordDO.getWord() + " ");
                }
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
