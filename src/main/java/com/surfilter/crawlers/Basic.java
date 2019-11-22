package com.surfilter.crawlers;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import org.seimicrawler.xpath.JXDocument;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Crawler(name = "basic")
public class Basic extends BaseSeimiCrawler {


   // @Scheduled(cron = "0/5 * * * * ?")
    public void callByCron(){
        logger.info("我是一个根据cron表达式执行的调度器，5秒一次");
        // 可定时发送一个Request
         push(Request.build(startUrls()[0],"start").setSkipDuplicateFilter(true));
    }
    public String[] urls;

    @Override
    public String[] startUrls() {
        return new String[]{"http://147xxoo.com"};
    }

    @Override
    public void start(Response response) {
        byte[] data = response.getData();
        System.out.println(data.toString());
        JXDocument doc = response.document();
        try {
            List<Object> urls = doc.sel("");
            logger.info("{}", urls.size());
            for (Object s:urls){
                push(Request.build(s.toString(),Basic::getTitle));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTitle(Response response){
        JXDocument doc = response.document();
        try {
            logger.info("url:{} {}", response.getUrl(), doc.sel("//h1[@class='postTitle']/a/text()|//a[@id='cb_post_title_url']/text()"));
            //do something
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
