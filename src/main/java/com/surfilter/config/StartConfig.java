package com.surfilter.config;

import com.surfilter.entity.WhiteUrl;
import com.surfilter.service.IIpService;
import com.surfilter.service.IWhiteListService;
import com.surfilter.util.SslUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;

/**
 * 初始化类
 */

@Configuration
@Slf4j
public class StartConfig {
    Set<String> qqset = new HashSet<>();
    @Autowired
    IWhiteListService whiteListService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisKeyInfo redisKeyInfo;
    @Autowired
    BaseInfo baseInfo;
    @Autowired
    IIpService ipService;
    private  static boolean isOSLinux;

    @PostConstruct
    public void init() {//将白名单放到 redis中。
        isOSLinux =  isOSLinux();
        try {
            SslUtils.ignoreSsl();//忽略所有证书。
        } catch (Exception e) {
            e.printStackTrace();
        }

        initQqSet();
        //配置文件路径。如果没有目录则创建文件目录。
        initFile();
        //加载白名单。
        initWhite();
        initCrawling();//初始化爬虫消费则。
       // initIp();
    }



    private void initQqSet() {
        qqset.add( "QQ" );
        qqset.add( "qq" );
    }

    private void initCrawling() {
//        Thread thread = new Thread( new Runnable() {
//            @Override
//            public void run() {
//                consumer.init();
//            }
//        } );
//        thread.start();
    }


    public static  boolean isOSLinux() {
        Properties prop = System.getProperties();

        String os = prop.getProperty("os.name");
        if (os != null && os.toLowerCase().indexOf("linux") > -1) {
            return true;
        } else {
            return false;
        }
    }

    private void initWhite() {
        boolean flag =  stringRedisTemplate.opsForHash().getOperations().hasKey( redisKeyInfo.getWhileUrl() );
        System.out.println(flag);
        if (flag) {
            stringRedisTemplate.opsForHash().getOperations().delete( redisKeyInfo.getWhileUrl() );
        }
        List<WhiteUrl> list = whiteListService.listWhiteUrl();
        Map<String,String> data = new HashMap<>();
        int count = 0 ;
        for (WhiteUrl wu :list) {
            data.put( wu.getUrl(),wu.getName() );
            count ++ ;
            if ( count % 2000 == 0 ){
                stringRedisTemplate.opsForHash().putAll(redisKeyInfo.getWhileUrl(),data);
                data.clear();
                count = 0;
            }
        }
        stringRedisTemplate.opsForHash().putAll(redisKeyInfo.getWhileUrl(),data);
        data.clear();
        log.info( "加载白名单信息成功" );
    }
    private void initFile() {
        File file = new File(baseInfo.getUrlReadPath());
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        File file1 = new File( baseInfo.getUrlSnapshot() );
        if(file1.isDirectory()) {
            file1.mkdirs();
        }
    }
    public Set<String> getQqset() {
        return qqset;
    }

    public void setQqset(Set<String> qqset) {
        this.qqset = qqset;
    }
}