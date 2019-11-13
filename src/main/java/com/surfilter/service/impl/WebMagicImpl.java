package com.surfilter.service.impl;

import com.surfilter.service.WebMagic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class WebMagicImpl implements WebMagic {


    @Override
    public void webMagicCrawler(String url) {
        Spider.create(new process()).addUrl(url).thread(5).run();
    }

    public static void main(String[] args) {
        new WebMagicImpl().webMagicCrawler("http://github.com/code4craft");
    }
    public class process implements PageProcessor{

        private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

        @Override
        public void process(Page page) {
            page.addTargetRequests(page.getHtml().links().regex("(http://github\\.com/\\w+/\\w+)").all());
            page.putField("author", page.getUrl().regex("http://github\\.com/(\\w+)/.*").toString());
            page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
            if (page.getResultItems().get("name")==null){
                //skip this page
                page.setSkip(true);
            }
            page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));
        }

        @Override
        public Site getSite() {
            return site;
        }
    }


}
