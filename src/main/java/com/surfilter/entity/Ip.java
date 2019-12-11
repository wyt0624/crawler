package com.surfilter.entity;

import lombok.Data;

@Data
public class Ip {
    private long id;
    private String startIp;
    private long  startIpNum;
    private String endIp;
    private long endIpNum;
    private String address;
    private String specificAddress;
    private String sourceType;
}
