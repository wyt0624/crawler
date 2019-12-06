package com.surfilter.config;

import com.surfilter.consumer.RedisReceiver;
import com.surfilter.entity.Ip;
import com.surfilter.entity.WhiteUrl;
import com.surfilter.service.IIpService;
import com.surfilter.service.IWhiteListService;
import com.surfilter.util.SslUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 初始化类
 */

@Configuration
@Slf4j
public class StartConfig {

    public static AtomicInteger atomicInteger = new AtomicInteger(0);
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
    @Autowired
    RedisReceiver redisReceiver;
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
        //initIp();
    }

    private void initIp() {
        log.info( "开始加载村真ip到redis" );
        int count = 0;
        for (;;) {
            List<Ip> list = ipService.listIps(count);
            if (list.size()> 0){
                Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
                for (Ip ip:list){
                    ZSetOperations.TypedTuple<String> tuple = new DefaultTypedTuple<String>(ip.getAddress() +
                            "_" + ip.getSpecificAddress() +"," + ip.getEndIpNum(), (double)ip.getEndIpNum());
                    tuples.add( tuple );
                }
                stringRedisTemplate.opsForZSet().add( redisKeyInfo.getFidelityIp() ,tuples);
                tuples.clear();
                tuples = null;
            }
            if (list.size()< 10000) {
                count += list.size();
                log.info( "存真ip入库成功，总条数：{}",count);
                break;
            } else {
                log.info( "存真ip入库成功入缓存，数量：{}",count);
                count += 10000;
            }
        }
    }

    private void initQqSet() {
        qqset.add( "QQ" );
        qqset.add( "qq" );
    }

    private void initCrawling() {
        Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {
                redisReceiver.init();
            }
        } );
        thread.start();
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
