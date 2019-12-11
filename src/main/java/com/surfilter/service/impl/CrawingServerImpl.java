package com.surfilter.service.impl;

import com.surfilter.config.BaseInfo;
import com.surfilter.config.BeanContext;
import com.surfilter.config.RedisKeyInfo;
import com.surfilter.config.StartConfig;
import com.surfilter.dao.InfoMapper;
import com.surfilter.entity.AnaliseInfo;
import com.surfilter.entity.Info;
import com.surfilter.enums.Param;
import com.surfilter.service.ICrawingService;
import com.surfilter.util.DateTimeUtil;
import com.surfilter.util.HttpUtil;
import com.surfilter.util.StringUtil;
import com.surfilter.whois.models.WhoisModel;
import com.surfilter.whois.utils.IpUtil;
import com.surfilter.whois.utils.WhoisUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
@Service
@Slf4j
@Data
public class CrawingServerImpl implements ICrawingService,Runnable{
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
    public List<String> list = new ArrayList<>(  );
    public CrawingServerImpl() {
    }
    public CrawingServerImpl(List<String> list ){
        this.list = list;
    }
    @Override
    public void run() {
        if (baseInfo == null) {
            baseInfo = BeanContext.getApplicationContext().getBean(BaseInfo.class);
        }
        if (startConfig == null ) {
            startConfig = BeanContext.getApplicationContext().getBean(StartConfig.class);
        }
        if (infoMapper == null) {
            infoMapper = BeanContext.getApplicationContext().getBean(InfoMapper.class);
        }
        if (redisKeyInfo == null) {
            redisKeyInfo = BeanContext.getApplicationContext().getBean(RedisKeyInfo.class);
        }
        if (stringRedisTemplate == null) {
            stringRedisTemplate = BeanContext.getApplicationContext().getBean(StringRedisTemplate.class);
        }
        try {
            List<Info> listInfo = new ArrayList<>();
            List<String> listAnaliseInfo = new ArrayList<>();
            for (String url : list) {
                try {
                    boolean white = stringRedisTemplate.opsForHash().hasKey( redisKeyInfo.getWhileUrl(), url );
                    if (white) {
                        continue;
                    }
                    Info info = new Info();
                    Document doc = null;
                    String title = "";
                    String html = "";
                    log.info( "爬取url:{}", url );
                    try {
                        String newUrl = null;
                        String port = "";
                        if (HttpUtil.isSocketAliveUitlitybyCrunchify( url, Param.HTTPS_PORT.getCode() )) {
                            newUrl = Param.HTTPS_PORT.getMsg() + "://" + url;
                        } else if (HttpUtil.isSocketAliveUitlitybyCrunchify( url, Param.HTTP_PORT.getCode() )) {
                            newUrl = Param.HTTP_PORT.getMsg() + "://" + url;
                        } else {
                            newUrl = Param.HTTP_PORT.getMsg() + "://" + url;
                        }
                        doc = HttpUtil.getDocByUrl( HttpUtil.getNewUrl( newUrl ), 10000 );
                        html = doc.html();
                        title = doc.title();
                        if (doc.select( "iframe" ).hasAttr( "src" )) { //如果html中包含ifream 标签
                            String ifreamUrl = doc.select( "iframe" ).first().attr( "src" );
                            if (!(ifreamUrl.contains( "https://" ) || ifreamUrl.contains( "http://" ))) {
                                if (ifreamUrl.startsWith( "/" )) {
                                    ifreamUrl = newUrl + ifreamUrl;
                                } else {
                                    ifreamUrl = newUrl + "/" + ifreamUrl;
                                }
                            }
                            try {
                                doc = HttpUtil.getDocByUrl( HttpUtil.getNewUrl( ifreamUrl ), 10000 );
                                html += doc.html();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        info.setIsConn( 1 );//可以访问。
                        String ip = IpUtil.getIpByDomain( url );
                        if (StringUtils.isNotBlank( ip )) {
                            info.setIp( ip );
//                            Set<String> adds = stringRedisTemplate.opsForZSet().rangeByScore( redisKeyInfo.getFidelityIp(),
//                                    StringUtil.getIpNum( ip ), Long.MAX_VALUE, 0, 1 );
//                            for (String address : adds) {
//                                info.setAddress( address );
//                                break;
//                            }
                            //adds.clear();
                        }
                        WhoisModel wm = WhoisUtil.queryWhois( url );
                        if (wm != null) {
                            if (DateTimeUtil.dateToTimstamp( wm.getCtime() ) != null && wm.getCtime() > 100000) {
                                info.setCreationTime( DateTimeUtil.dateToTimstamp( wm.getCtime() ) );
                            }
                            if (DateTimeUtil.dateToTimstamp( wm.getEtime() ) != null && wm.getCtime() > 100000) {
                                info.setExpireTime( DateTimeUtil.dateToTimstamp( wm.getEtime() ) );
                            }
                            if (StringUtils.isNotBlank( wm.getPhone() )) {
                                info.setTel( wm.getPhone() );
                            }
                            if (StringUtils.isNotBlank( wm.getEmail() )) {
                                info.setEmail( wm.getEmail() );
                            }
                            if (DateTimeUtil.dateToTimstamp( wm.getUtime() ) != null && wm.getCtime() > 100000) {
                                info.setLastUpdateTime( DateTimeUtil.dateToTimstamp( wm.getUtime() ) );
                            }
                        }
                        info.setIsWhois( 1 );
                        info.setPort( port );
                        title = new String( title.getBytes(),"UTF-8");
                        if(title.length()> 499) {
                            info.setTitle( title.substring( 0, 499 ) );
                        }
                        //抓取网页上手机号码。
                        //String phones = StringUtil.phoneRegEx( html );
//                        if (phones.length() > 498) {
//                            info.setWebPhone( phones.substring( 0, 498 ) );
//                        } else {
//                            info.setWebPhone( phones );
//                        }
                        //抓取网上的QQ号码。
//                        String qqs = StringUtil.qqRegEx( html, startConfig.getQqset() );
//                        if (StringUtils.isNotBlank( qqs )) {
//                            if (qqs.length() > 449) {
//                                info.setQq( qqs.substring( 0, 449 ) );
//                            } else {
//                                info.setQq( qqs );
//                            }
//                        }
//                        wm = null;
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        info.setIsConn( 2 );
                    }
                    info.setUrl( url );
                    listInfo.add( info );
                    if (info.getIsConn() == 1) { //能访问。 发送分析队列。
                        AnaliseInfo analiseInfo = new AnaliseInfo();
                        analiseInfo.setTitle( title );
                        analiseInfo.setUrl( url );
                        //analiseInfo.setHtml( StringUtil.RemoveSymbolCh( html ) );
                        analiseInfo.setHtml( html );
                        //域名清理。 符合域名规则的 将数据放到 队列中。
                        StringUtil.domainExClean( url,info );
                        if (info.getRuleCount() >= 5 && // 域名规则大约等于5的才可以进入
                                !title.equals( "IIS7" ) && //
                                !title.equals( "Welcome to nginx!") &&
                                !title.equals( "登录到 Outlook" )
                        ) {
                            String webInfo = url+"|"+html;
                            listAnaliseInfo.add( webInfo );
                        }
                    }
                }catch ( Exception e) {
                    e.printStackTrace();
                }
            }
            if (listInfo.size() > 0) {
                try {
                    infoMapper.addListInfo( listInfo );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            listInfo.clear();
            if (listAnaliseInfo.size() > 0) {
//                String json = JSON.toJSONString( listAnaliseInfo );
//                try {
//                    stringRedisTemplate.opsForList().rightPush( redisKeyInfo.getAnaliseQueue(), json );
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                for (String stt: listAnaliseInfo) {
                    try {
                        stringRedisTemplate.opsForList().rightPush( redisKeyInfo.getAnaliseQueue(), stt );
                    } catch ( Exception e) {
                        e.printStackTrace();
                        stringRedisTemplate.opsForList().rightPush( redisKeyInfo.getAnaliseQueue(), stt );
                    }
                }
            }
            listAnaliseInfo.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StartConfig.atomicInteger.decrementAndGet();
            String[] strings = new String[list.size()];
            list.toArray(strings);
            stringRedisTemplate.opsForSet().remove( redisKeyInfo.getCrawlerCache(),strings );
            strings=null;
            list.clear();
        }
    }
}
