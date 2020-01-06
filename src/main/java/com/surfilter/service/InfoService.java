package com.surfilter.service;

import com.surfilter.entity.Columns;
import com.surfilter.entity.Info;

import java.util.List;

public interface InfoService {
    /***
     * 结果列表
     * @return
     * @param columns
     */
    List<Info> listInfo(Columns columns,int catagoryType);

    /**
     * 初始化统计域名信息展示到地图点上。
     * @return
     */
    List<Info> listInfoCount(Integer catagoryType);
}
