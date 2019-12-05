package com.surfilter.service;

import com.surfilter.entity.Ip;

import java.util.List;

public interface IIpService {
    List<Ip> listIps(int count);
}
