package com.surfilter.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DomainUrl {
    private Long id;  //id
    private String Url; //域名
    private Timestamp operateTime; //操作时间
    private int crawlingStatus; //爬取状态。  1是已经爬取 2 是没有爬取
    private int cacheStatus;//缓存状态 1是已经缓存。 2 是没有缓存。
}
