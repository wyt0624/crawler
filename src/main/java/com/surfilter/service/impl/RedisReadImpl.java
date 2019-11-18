package com.surfilter.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.surfilter.dataobject.WordDO;
import com.surfilter.enums.Param;
import com.surfilter.service.RedisRead;
import com.surfilter.service.Word;
import com.surfilter.util.HttpUtil;
import com.surfilter.util.RedisUtil;
import com.surfilter.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;


import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisReadImpl implements RedisRead {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ChineseParticipleImpl chineseParticiple;

    @Override
    public boolean readRedis(String key) {
        long len = redisUtil.lGetListSize(key);
        boolean flag = false;
        if (len > 0) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * @param key
     */
    @Override
    public void startCrawler(String key) {
        String ips = redisTemplate.opsForList().rightPop(key,1L, TimeUnit.SECONDS);
        Set<String> urlList = new Gson().fromJson(ips, new TypeToken<Set<String>>(){}.getType());
        BufferedWriter bw = null;
        for (String url : urlList) {
            //爬取当前url
            try {
                Set<WordDO> set = getWebMessageText(url);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if(bw != null) {
                        bw.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }


    /**
     * 爬取url，获取文本信息
     * @param url
     */
    @Override
    public Set<WordDO> getWebMessageText(String url) {
        HttpUtil.trustEveryone();
        Set<WordDO> set = new HashSet<>();
        try {
            //判断端口 转换url
            String newUrl = HttpUtil.getNewUrl(url);
            Document doc = HttpUtil.getDocByUrl(newUrl);
            //获取所有节点元素
            Elements elementAll = doc.getAllElements();
            for (Element element : elementAll) {
                String text = element.text();
                //分词 去除特殊符号
                List<String> wordList = chineseParticiple.getChineseWord(StringUtil.RemoveSymbol(text));
                for (String word : wordList) {
                    WordDO wordDO = new WordDO();
                    wordDO.setWord(word);
                    set.add(wordDO);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }

    public static void main(String[] args) {
        Set<WordDO> set = new RedisReadImpl().getWebMessageText("http://fsxys.com/");
        for (WordDO wordDO : set) {
            System.out.println(wordDO.getWord());
        }
    }





}
