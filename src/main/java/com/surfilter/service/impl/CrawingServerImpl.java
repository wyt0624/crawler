package com.surfilter.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.surfilter.config.BaseInfo;
import com.surfilter.config.RedisKeyInfo;
import com.surfilter.config.StartConfig;
import com.surfilter.dao.InfoMapper;
import com.surfilter.entity.AnaliseInfo;
import com.surfilter.entity.Info;
import com.surfilter.enums.Param;
import com.surfilter.service.ICrawingService;
import com.surfilter.util.*;
import com.surfilter.whois.models.WhoisModel;
import com.surfilter.whois.utils.WhoisUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Data
public class CrawingServerImpl implements ICrawingService{
    @Autowired
    BaseInfo baseInfo;
    @Autowired
    StartConfig startConfig;
    @Autowired
    InfoMapper infoMapper;
    @Autowired
    RedisKeyInfo redisKeyInfo;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Async()
    public void crawingUrl(String url) {

        boolean ispass = Ping.ping(  url,2,3000);
        Info info = new Info();
        Document doc = null;
        String title = "";
        String html= "";
        if (ispass){
            log.info( "爬取路径" +url );
            try {
                String newUrl = null;
                String port = "";
                if (HttpUtil.isSocketAliveUitlitybyCrunchify(url,Param.HTTPS_PORT.getCode())) {
                    newUrl = Param.HTTPS_PORT.getMsg() + "://" + url;
                    port = Param.HTTPS_PORT.getMsg();
                } else  if (HttpUtil.isSocketAliveUitlitybyCrunchify(url,Param.HTTP_PORT.getCode())){
                    newUrl = Param.HTTP_PORT.getMsg() + "://" + url;
                    port = Param.HTTP_PORT.getMsg();
                } else {
                    newUrl = Param.HTTP_PORT.getMsg() + "://" + url;
                    port = Param.HTTP_PORT.getMsg();
                }
                doc = HttpUtil.getDocByUrl(HttpUtil.getNewUrl(newUrl),10000);
                html= doc.html();
                title = doc.title();
                if (doc.select( "iframe" ).hasAttr( "src" )) { //如果html中包含ifream 标签
                    String ifreamUrl = doc.select( "iframe" ).first().attr( "src" );
                    if (!(ifreamUrl.contains( "https://" ) || ifreamUrl.contains( "http://" ))) {
                        ifreamUrl = url + ifreamUrl;
                    }
                    try {
                        doc = HttpUtil.getDocByUrl( HttpUtil.getNewUrl( ifreamUrl ), 10000 );
                        html += doc.html();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                info.setIsConn( 1 );//可以访问。
                WhoisModel wm = WhoisUtil.queryWhois(url);
                if (wm != null) {
                    if (DateTimeUtil.dateToTimstamp( wm.getCtime() ) != null) {
                        info.setCreationTime( DateTimeUtil.dateToTimstamp( wm.getCtime() ) );
                    }
                    if (DateTimeUtil.dateToTimstamp( wm.getEtime() ) != null) {
                        info.setExpireTime( DateTimeUtil.dateToTimstamp( wm.getEtime() ) );
                    }
                    if (StringUtils.isNotBlank( wm.getPhone() )) {
                        info.setTel( wm.getPhone() );
                    }
                    if (StringUtils.isNotBlank( wm.getEmail() )) {
                        info.setEmail( wm.getEmail() );
                    }
                    if (StringUtils.isNotBlank( wm.getIp() )) {
                        info.setIp( wm.getIp() );
                    }
                    if ( DateTimeUtil.dateToTimstamp(wm.getUtime()) !=  null) {
                        info.setLastUpdateTime(DateTimeUtil.dateToTimstamp(wm.getUtime()));
                    }
                }
                info.setPort( port );
                info.setTitle( title );
//                info.setFileName( filename );
                //抓取网页上手机号码。
                String phones = StringUtil.phoneRegEx(html);
                info.setWebPhone( phones );
                //抓取网上的QQ号码。
                String qqs = StringUtil.qqRegEx(html,startConfig.getQqset());
                info.setQq( qqs );
                //抓取网页上的地址。
                //String address = StringUtil.addressRegEx( html );
                wm = null;

            } catch (Exception e1) {
                e1.printStackTrace();
                info.setIsConn( 2 );
            }
        } else {
            //保存数据
            info.setIsConn( 2 ); // 2 是不可以连接。
        }
        info.setUrl( url );
        infoMapper.addInfo(info);
        if (info.getIsConn()  == 1 ){ //能访问。 发送分析队列。
            AnaliseInfo analiseInfo = new AnaliseInfo();
            analiseInfo.setTitle( title );
            analiseInfo.setUrl( url );
            analiseInfo.setHtml( StringUtil.RemoveSymbolCh(html) );
            JSONObject json = (JSONObject) JSON.toJSON( analiseInfo );
            stringRedisTemplate.opsForList().rightPush(redisKeyInfo.getAnaliseQueue(),json.toString());
        }
    }
}
