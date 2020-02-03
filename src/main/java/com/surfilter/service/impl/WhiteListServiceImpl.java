package com.surfilter.service.impl;


import com.surfilter.dao.WhiteUrlMapper;
import com.surfilter.entity.Columns;
import com.surfilter.entity.Phishing;
import com.surfilter.entity.WhiteUrl;
import com.surfilter.service.IWhiteListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<WhiteUrl> listWhite(Columns columns) {
        String content = null;
        if(columns.getSearch().values().size()>1) {
            Iterator it =   columns.getSearch().values().iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                if (obj instanceof String){
                    if (!obj.equals( "false" )){
                        content= obj +"";
                    }
                }
            }
        }
        Map<String,Object> param = new HashMap<>(  );
        param.put( "content",content );
        return whiteUrlMapper.listWhite(param);
    }

    @Override
    public List<Phishing> listBlack(Columns columns) {
        String content = null;
        if(columns.getSearch().values().size()>1) {
            Iterator it =   columns.getSearch().values().iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                if (obj instanceof String){
                    if (!obj.equals( "false" )){
                        content= obj +"";
                    }
                }
            }
        }
        Map<String,Object> param = new HashMap<>(  );
        param.put( "content",content );
        List<Phishing> list = whiteUrlMapper.listBlack(param);
        return list;
    }
}
