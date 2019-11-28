package com.surfilter.job;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CrawlerUrl implements Runnable {
    @Override
    public void run() {

    }

//    private String url ;
//    private String key ;
//    public CrawlerUrl(String url,String key) {
//        this.url = url;
//        this.key = key;
//    }
//
//    @Override
//    public void run() {
//        ScheduledTasks.atomicLong.getAndDecrement();
//        boolean ispass = Ping.ping(  url,2,3000);
//        log.info( "爬取路径" +url );
//        if (ispass) {
//            Document doc = null;
//            try {
//                doc = HttpUtil.getDocByUrl(HttpUtil.getNewUrl(url),10000);
//                String html = doc.html();
//                if (doc.select( "iframe" ).hasAttr( "src" )) { //如果html中包含ifream 标签
//                    String ifreamUrl = doc.select( "iframe" ).first().attr( "src" );
//                    try {
//                        Document doc1 = HttpUtil.getDocByUrl( HttpUtil.getNewUrl( ifreamUrl ), 10000 );
//                        html += doc1.html();
//                    } catch(Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (StringUtils.isNotBlank(html)) {
//                    //保存快照。
//                    String filepath =  MyApplicationRunner.writeFilepath + url.replace("\"","") + "_" + System.currentTimeMillis() + ".txt";
//                    FileUtil.saveSnapshot( html,filepath );
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }
//            finally {
//            }
//        } else {
//            //无法打开网页。
//        }
//    }
}
