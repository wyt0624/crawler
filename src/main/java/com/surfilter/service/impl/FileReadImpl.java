package com.surfilter.service.impl;

import com.surfilter.config.BaseInfo;
import com.surfilter.enums.Param;
import com.surfilter.service.FileRead;
import com.surfilter.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class FileReadImpl implements FileRead {

    @Autowired
    BaseInfo baseInfo;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     *
     *
     * // 如入缓存， 入队列， 入mysql
     */
    public void doMainToRedis() {
        File file=new File(baseInfo.getUrlReadPath());
        File[] files = file.listFiles(new FilenameFilter(){
            @Override
            public boolean accept(File dir, String name) {
                return !name.endsWith(".bak");
            }
        });
        Set<String> domainList=new HashSet<>();
        try {
            for(File tempFile:files){
                //if(tempFile.getName().contains("bak")){continue;}
                FileReader fr = new FileReader(tempFile.getPath());
                BufferedReader bf = new BufferedReader(fr);
                String str;
                // 按行读取字符串
                while ((str = bf.readLine()) != null) {
                    domainList.add(str.replace("\"",""));
                    if(domainList.size()>=1000){
                        String distSeg = GsonUtil.getJsonStringByObject(domainList);
                        //入消息redis队列

                        stringRedisTemplate.opsForList().rightPush(Param.REDIS_URL.getMsg(),distSeg);
                        domainList.clear();
                    }
                }
                String distSeg = GsonUtil.getJsonStringByObject(domainList);
                stringRedisTemplate.opsForList().rightPush(Param.REDIS_URL.getMsg(),distSeg);
                domainList.clear();
                bf.close();
                fr.close();
                tempFile.renameTo(new File(tempFile.getPath()+".bak"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
