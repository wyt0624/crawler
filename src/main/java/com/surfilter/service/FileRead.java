package com.surfilter.service;

public interface FileRead {

    /**
     * 扫描目录下的没有.bak得文件
     * 按行读取
     * 1000条入一次redis
     */
    public void doMainToRedis();
}
