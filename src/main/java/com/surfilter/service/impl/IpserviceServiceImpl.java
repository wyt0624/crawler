package com.surfilter.service.impl;

import com.surfilter.dao.IpMapper;
import com.surfilter.entity.Ip;
import com.surfilter.service.IIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class IpserviceServiceImpl implements IIpService {
    /**
     *
     */
    @Autowired
    private IpMapper ipMapper;

    @Override
    public List<Ip> listIps(int count) {
        return ipMapper.ListIps( count );
    }
}
