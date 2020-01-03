package com.surfilter.service.impl;

import com.surfilter.dao.InfoMapper;
import com.surfilter.entity.Info;
import com.surfilter.service.InfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class InfoServiceImpl implements InfoService {
    @Autowired
    InfoMapper infoMapper;
    @Override
    public List<Info> listInfo() {
        return infoMapper.listInfo();
    }
}
