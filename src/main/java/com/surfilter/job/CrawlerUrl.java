package com.surfilter.job;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.surfilter.MyApplicationRunner;
import com.surfilter.dataobject.WordDO;
import com.surfilter.service.RedisRead;
import com.surfilter.util.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
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


    @Value("${job.param.write-file-path}")
    private String writeFilePath;

    @Value("${job.param.yes-file-path}")
    private String writeYesFilePath;


    @Override
    public void run() {
        ScheduledTasks.atomicLong.getAndDecrement();
        Document doc = null;
        String text = null;
        try {
            doc = HttpUtil.getDocByUrl(HttpUtil.getNewUrl(url),5000);
        } catch (IOException e) {
            try {
                doc = HttpUtil.getDocByUrl(HttpUtil.getNewUrl(url),8000);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        try {
            text = StringUtil.RemoveSymbol(doc.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (text != null) {
            BufferedWriter bw = null;
            if (!text.isEmpty()) {
              //  Set<WordDO> wordDOSet = redisRead.getWebMessageWord(url);
                if (text != null) {
                    File file = new File(writeFilePath + url.replaceAll("\"","") + "_" + System.currentTimeMillis() + ".txt");
                    File yesFile = null;
                    try {
                        bw = new BufferedWriter(new FileWriter(file));
                        if(!file.exists()) {
                            file.createNewFile();
                        }
                        Patterns vpnPatterns = new Patterns(MyApplicationRunner.vpnWordList);
                        Patterns yellowPatterns = new Patterns(MyApplicationRunner.yellowWordList);
                        Patterns wadingPatterns = new Patterns(MyApplicationRunner.wadingListList);
                        Patterns whitePatterns = new Patterns(MyApplicationRunner.whiteWordListList);
                        Set<Keyword> vpnList =  vpnPatterns.searchKeyword(doc.toString(),null);
                        Set<Keyword> yellowList =  yellowPatterns.searchKeyword(doc.toString(),null);
                        Set<Keyword> waddingList =  wadingPatterns.searchKeyword(doc.toString(),null);
                        Set<Keyword> whiteList = whitePatterns.searchKeyword(doc.toString(),null);
                        bw.write(text);
//                        for (WordDO wordDO : wordDOSet) {
//                            bw.write(wordDO.getWord() + " ");
//                        }
//                        FileUtil.writeKeyWordToFile(bw,vpnList);
//                        FileUtil.writeKeyWordToFile(bw,yellowList);
//                        FileUtil.writeKeyWordToFile(bw,waddingList);

//                        if (!MyApplicationRunner.vpnWordList.isEmpty()) {
//                            bw.write("vpn词语所占比例" + MathUtil.accuracy(vpnList.size(),MyApplicationRunner.vpnWordList.size(),2) + "\r\n");
//                        }
//                        if (!MyApplicationRunner.wadingListList.isEmpty()) {
//                            bw.write("涉黄词语所占比例" + MathUtil.accuracy(yellowList.size(),MyApplicationRunner.yellowWordList.size(),2) + "\r\n");
//                        }
//                        if (!MyApplicationRunner.wadingListList.isEmpty()) {
//                            bw.write("涉赌词语所占比例" + MathUtil.accuracy(waddingList.size(),MyApplicationRunner.wadingListList.size(),2) + "\r\n");
//                        }

                        bw.flush();
                        boolean isFlag = (!vpnList.isEmpty() || !yellowList.isEmpty() || !waddingList.isEmpty()) && whiteList.isEmpty() && yellowList.size() > 1;
                        if (isFlag) {
                            yesFile = new File(writeYesFilePath + url.replaceAll("\"","") + "_" + System.currentTimeMillis() + ".txt");
                            if (!yesFile.exists()) {
                                yesFile.createNewFile();
                            }
                            FileUtil.copyFileUsingFileChannels(file,yesFile);
                        }
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

    }
}
