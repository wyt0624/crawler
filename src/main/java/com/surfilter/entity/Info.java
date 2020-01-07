package com.surfilter.entity;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class Info {
    private long id;//id
    private String url;//域名
    private int isConn;//是否可连接
    private String port;//注册端口
    private String email;//注册邮箱。
    private String tel;//注册电话
    private String registrar;//注册人
    private Timestamp creationTime;//创建时间
    private Timestamp expireTime;//过期时间
    private String company;//公司名称。
    private String ip;//域名的ip
    private String fileName;//快照名称。
    private Timestamp lastUpdateTime;//最后更新时间，
    private String webPhone;//网页爬取的手机号码
    private String qq;//qq
    private String wx;//微信
    private String address;//爬取网页上的地址。
    private String title;//标题
    private int category;//0 网站类型。 0 是正常网站， 1是 赌博网站， 2 是黄色网站。
    private int isWhois;//是否已经爬取whois 网站了。
    private int isPort;// 是否已经爬取nmap短偶库了。
    private String rule; //域名符合规则。
    private int ruleCount;//域名符合规则数量
    private String lngX;//x经纬度
    private String lngY;//y经纬度
    private int num;//统计数量
    private String httpProtocol;
    private Date createTime;
}
