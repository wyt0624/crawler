package com.surfilter.service.impl;

import com.surfilter.MyApplicationRunner;
import com.surfilter.enums.Param;
import com.surfilter.service.FileRead;
import com.surfilter.util.GsonUtil;
import com.surfilter.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class FileReadImpl implements FileRead {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 扫描目录下的没有.bak得文件
     * 按行读取
     * 1000条入一次redis
     * @param path
     */
    public void doMainToRedis(String path) {
        File file=new File(path);
        File[] files = file.listFiles();
        Set<String> domainList=new HashSet<>();
        try {
            for(File tempFile:files){
                if(tempFile.getName().contains("bak")){continue;}
                FileReader fr = new FileReader(tempFile.getPath());
                BufferedReader bf = new BufferedReader(fr);
                String str;
                // 按行读取字符串
                while ((str = bf.readLine()) != null) {
                   // if (StringUtil.isWhiteUrl(str)) {
                    domainList.add(str.replaceAll("\"",""));
                    if(domainList.size()>=1000){
                        String distSeg = GsonUtil.getJsonStringByObject(domainList);
                        stringRedisTemplate.opsForList().rightPush(Param.REDIS_URL.getMsg(),distSeg);
                        domainList.clear();
                    }
                    //}
                }
                bf.close();
                fr.close();
                tempFile.renameTo(new File(tempFile.getPath()+".bak"));
                String distSeg = GsonUtil.getJsonStringByObject(domainList);
                stringRedisTemplate.opsForList().rightPush(Param.REDIS_URL.getMsg(),distSeg);
                domainList.clear();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
