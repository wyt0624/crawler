package com.surfilter.entity;
import lombok.Data;
/**
 * 模板基础类。
 */
@Data
public class Template {
    private Long id;//模板id
    private String url;//网站地址 。
    private String name;//网站名称。
    private String title;//网站标题
    private String keyword;//关键词；
    private String domain;//域名;
}
