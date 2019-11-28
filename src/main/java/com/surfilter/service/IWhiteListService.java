package com.surfilter.service;

import com.surfilter.entity.WhiteUrl;

import java.util.List;

public interface IWhiteListService {

    /**
     * 查询所有白名单。
     */
    List<WhiteUrl> listWhiteUrl();


}
