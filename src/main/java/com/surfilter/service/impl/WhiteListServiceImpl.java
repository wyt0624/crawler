package com.surfilter.service.impl;


import com.surfilter.dao.WhiteUrlMapper;
import com.surfilter.entity.WhiteUrl;
import com.surfilter.service.IWhiteListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("ALL")
@Service
@Slf4j
public class WhiteListServiceImpl implements IWhiteListService {
    @Autowired
    WhiteUrlMapper whiteUrlMapper;

    @Override
    public List<WhiteUrl> listWhiteUrl() {
        return whiteUrlMapper.listWhiteUrl();
    }
}
