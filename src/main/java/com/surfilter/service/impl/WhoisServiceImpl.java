package com.surfilter.service.impl;

import com.surfilter.dao.WhoisMapper;
import com.surfilter.entity.Info;
import com.surfilter.service.IWhoisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("ALL")
@Service
@Slf4j
public class WhoisServiceImpl implements IWhoisService {
    @Autowired
    WhoisMapper whoisMapper;
    @Override
    public List<Info> listWhois() {
        return whoisMapper.listWhois();
    }

    @Override
    public void updateWhoisInfo(List<Info> listinfo) {
      whoisMapper.updatelistWhois(listinfo);
    }
}
