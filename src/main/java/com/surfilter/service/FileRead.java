package com.surfilter.service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.surfilter.enums.Param;
import com.surfilter.util.ApplicationContextUtil;
import com.surfilter.util.GsonUtil;
import com.surfilter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public interface FileRead {

    /**
     * 扫描目录下的没有.bak得文件
     * 按行读取
     * 1000条入一次redis
     * @param path
     */
    public void doMainToRedis(String path);


}
