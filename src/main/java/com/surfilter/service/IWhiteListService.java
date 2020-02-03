package com.surfilter.service;

import com.surfilter.entity.Columns;
import com.surfilter.entity.Phishing;
import com.surfilter.entity.WhiteUrl;

import java.util.List;

public interface IWhiteListService {

    /**
     * 查询所有白名单。
     */
    List<WhiteUrl> listWhiteUrl();

    /**分页查询白名单
     *
     */
    List<WhiteUrl> listWhite(Columns columns);

    /**
     * 分页查询黑名单
     * @param columns
     * @return
     */
    List<Phishing> listBlack(Columns columns);
}
