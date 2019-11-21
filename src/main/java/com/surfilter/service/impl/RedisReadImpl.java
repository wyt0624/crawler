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
                Set<WordDO> set = getWebMessageWord(url);
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



    @Override
    public Set<WordDO> getWebMessageWord(String url) {
        Set<WordDO> set = new HashSet<>();
        String text = getWebMessageText(url);
        //分词 去除特殊符号
        List<String> wordList = null;
        try {
            wordList = chineseParticiple.getChineseWord(StringUtil.RemoveSymbol(text));
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String word : wordList) {
            WordDO wordDO = new WordDO();
            wordDO.setWord(word);
            set.add(wordDO);
        }
        return set;
    }



    @Override
    public String getWebMessageText(String url) {
        HttpUtil.trustEveryone();
        String text = null;
        //判断端口 转换url
        String newUrl = HttpUtil.getNewUrl(url);
        Document doc = HttpUtil.getPageContent(newUrl);
        //获取所有节点元素
        Elements elements = doc.select("head > script");
        Elements elementAll = doc.getAllElements();
        Elements elementsByAttribute = doc.getElementsByAttribute("a");
        for (Element element : elementAll) {
            String textTmp = element.text();
            if (!textTmp.isEmpty()) {
                text = textTmp + text;
            }
        }

        return text;
    }

    public static void main(String[] args) {
        String text = new RedisReadImpl().getWebMessageText("173wyt.com");
        System.out.println(text);
    }





}
