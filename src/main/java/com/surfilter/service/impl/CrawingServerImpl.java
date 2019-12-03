package com.surfilter.service.impl;

import com.surfilter.config.BaseInfo;
import com.surfilter.config.StartConfig;
import com.surfilter.dao.InfoMapper;
import com.surfilter.entity.Info;
import com.surfilter.enums.Param;
import com.surfilter.service.ICrawingService;
import com.surfilter.util.*;
import com.surfilter.whois.models.WhoisModel;
import com.surfilter.whois.utils.WhoisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CrawingServerImpl implements ICrawingService {
    @Autowired
    BaseInfo baseInfo;
    @Autowired
    StartConfig startConfig;
    @Autowired
    InfoMapper infoMapper;

    @Async(value="threadPool")
    @Override
    public void crawingUrl(String url) {
        boolean ispass = Ping.ping(  url,2,3000);
        Info info = new Info();
        if (ispass){
            log.info( "爬取路径" +url );
            Document doc = null;
            try {
                String newUrl = null;
                String port = "";
                if (HttpUtil.isSocketAliveUitlitybyCrunchify(url,Param.HTTPS_PORT.getCode())) {
                    newUrl = Param.HTTPS_PORT.getMsg() + "://" + url;
                    port = Param.HTTPS_PORT.getMsg();
                } else if (HttpUtil.isSocketAliveUitlitybyCrunchify(url,Param.HTTP_PORT.getCode())) {
                    newUrl = Param.HTTP_PORT.getMsg() + "://" + url;
                    port = Param.HTTP_PORT.getMsg();
                }  else {

                    newUrl = url;
                }
                doc = HttpUtil.getDocByUrl(HttpUtil.getNewUrl(url),10000);
                String html = doc.html();
                if (doc.select( "iframe" ).hasAttr( "src" )) { //如果html中包含ifream 标签
                    String ifreamUrl = doc.select( "iframe" ).first().attr( "src" );
                    try {
                        Document doc1 = HttpUtil.getDocByUrl( HttpUtil.getNewUrl( ifreamUrl ), 10000 );
                        html += doc1.html();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                //whois
                String filename = StringUtil.filename( url );
                if (StringUtils.isNotBlank(html)) {
                    //保存快照。
                    FileUtil.saveSnapshot( html,baseInfo.getUrlSnapshot() + filename );
                }
                WhoisModel wm = WhoisUtil.queryWhois(url);
                info.setCreationTime( DateTimeUtil.dateToTimstamp(wm.getCtime()) );
                info.setExpireTime( DateTimeUtil.dateToTimstamp(wm.getEtime()) );
                info.setTel( wm.getPhone() );
                info.setEmail( wm.getEmail() );
                info.setIp( wm.getIp() );
                info.setLastUpdateTime( DateTimeUtil.dateToTimstamp(wm.getUtime()) );
                info.setSnapshotName( filename );
                //抓取网页上手机号码。
                String phones = StringUtil.phoneRegEx(html);
                info.setWebPhone( phones );
                //抓取网上的QQ号码。
                String qqs = StringUtil.qqRegEx(html,startConfig.getQqset());
                wm = null;
        } catch (Exception e1) {
            e1.printStackTrace();
            info.setIsConn( 2 );
        }
        finally {


        }
        } else {
            //保存数据
            info.setIsConn( 2 ); // 2 是不可以连接。
        }
        //infoMapper.addInfo(info);

    }
}
