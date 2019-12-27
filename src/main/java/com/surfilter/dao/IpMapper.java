package com.surfilter.dao;

import com.surfilter.entity.Ip;

import java.util.List;

public interface IpMapper {
    List<Ip> ListIps(int count);

    void addListIps(List<Ip> list);
}
