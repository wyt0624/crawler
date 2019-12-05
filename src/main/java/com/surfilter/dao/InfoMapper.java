package com.surfilter.dao;

import com.surfilter.entity.Info;

import java.util.List;

public interface InfoMapper {
    void addInfo(Info info);

    void addListInfo(List<Info> listInfo);
}
