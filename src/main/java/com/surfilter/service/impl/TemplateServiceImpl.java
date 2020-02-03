package com.surfilter.service.impl;

import com.surfilter.dao.TemplateMapper;
import com.surfilter.entity.Columns;
import com.surfilter.entity.Template;
import com.surfilter.service.ITemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TemplateServiceImpl  implements ITemplateService {
    @Autowired
    TemplateMapper templateMapper;
    @Override
    public List<Template> listTemplate(Columns columns) {
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
        List<Template> list = templateMapper.listTemplate(param);
        return list;
    }

    @Override
    public Map<String, Object> optTemplate(Template template) throws Exception {
        Map<String, Object> map = new HashMap<>();
        //如果是 > 0 说明是修改。否则认为是更新。
        String host = null;
        try {
            host = new URL( template.getUrl() ).getHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        template.setDomain( host );
        if (template.getId() != null && template.getId() > 0) {//更新
            templateMapper.changeTemplate( template );
        } else {//新建
            templateMapper.addTemplate( template );
        }
        return map;
    }

    @Override
    public Map<String, Object> checkUrl(Template template) {
        Map<String,Object> map = new HashMap<>(  );
        int count = templateMapper.checkUrl( template );
        map.put( "num",count );
        return map;
    }

    @Override
    public Map<String, Object> delTemplate(Template template) {
        Map<String,Object> map = new HashMap<>(  );
        templateMapper.delTemplate(template);
        return map;
    }
}
