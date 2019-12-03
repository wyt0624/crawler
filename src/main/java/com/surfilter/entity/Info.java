package com.surfilter.entity;

import lombok.Data;

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
    private String snapshotName;//快照名称。
    private Timestamp lastUpdateTime;//最后更新时间，
    private String webPhone;//网页爬取的手机号码
    private String qq;//qq
    private String wx;//微信
    private String address;//爬取网页上的地址。
}
