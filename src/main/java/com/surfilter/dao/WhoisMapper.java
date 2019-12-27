package com.surfilter.dao;

import com.surfilter.entity.Info;

import java.util.List;

public interface WhoisMapper {
    List<Info> listWhois(Long maxId);

    void updatelistWhois(List<Info> listinfo);
}
