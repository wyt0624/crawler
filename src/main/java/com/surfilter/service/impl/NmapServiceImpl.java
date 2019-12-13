package com.surfilter.service.impl;

import com.surfilter.dao.NmapMapper;
import com.surfilter.entity.Info;
import com.surfilter.service.INmapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NmapServiceImpl implements INmapService {

    @Autowired
    NmapMapper nmapMapper;
    @Override
    public List<Info> listNmap() {
        return nmapMapper.listNmap();
    }

    @Override
    public void updateListNmap(List<Info> list) {
         nmapMapper.updateListNmap(list);
    }
}
