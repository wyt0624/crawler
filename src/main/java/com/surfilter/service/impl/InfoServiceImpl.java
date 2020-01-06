package com.surfilter.service.impl;

import com.surfilter.dao.InfoMapper;
import com.surfilter.entity.Columns;
import com.surfilter.entity.Info;
import com.surfilter.service.InfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class InfoServiceImpl implements InfoService {
    @Autowired
    InfoMapper infoMapper;
    @Override
    public List<Info> listInfo(Columns columns,int catagoryType) {
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
        param.put( "catagoryType",catagoryType );
        return infoMapper.listInfo(param);
    }

    @Override
    public List<Info> listInfoCount(Integer catagoryType) {
        Map<String,Object> param = new HashMap<>(  );
        param.put( "catagoryType",catagoryType );
        return infoMapper.listInfoCount(param);
    }
}
