package com.surfilter.enums;

public enum Param implements CommonParam{

    REDIS_RESULT_RES(10000, "domain_res"),
    REDIS_RESULT_SSR(20000, "domain_ss"),
    REDIS_URL(30000, "domain_url"),


    HTTP_PORT(80,"http"),
    HTTPS_PORT(443,"https"),

    VPN_WORD(1,"VPN词语"),
    YELLOW_WORD(2,"涉黄词语"),
    WADING_WORD(3,"涉赌词语"),
    WHITE_WORD(4,"白名单词语"),
    ;

    private Param(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }


}
