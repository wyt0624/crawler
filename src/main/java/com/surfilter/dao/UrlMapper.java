package com.surfilter.dao;

import com.surfilter.dataobject.UrlDO;

import java.util.List;

public interface UrlMapper {

    List<UrlDO> selectUrlListByType(Integer type);
}
