package com.surfilter.service;

import com.surfilter.entity.Info;

import java.util.List;

public interface IWhoisService {
    List<Info> listWhois();

    void updateWhoisInfo(List<Info> listinfo);
}
