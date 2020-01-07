package com.surfilter.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.surfilter.entity.Columns;
import com.surfilter.entity.Info;
import com.surfilter.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class indexController {
    @Autowired
    InfoService infoService;

    @RequestMapping("index")
    public String index1(@RequestParam(value="category",required=false) Integer catagoryType, ModelMap mv) {
        List<Info> list = null;
        if (catagoryType == null){
            catagoryType= 0;
        }
        try {
            list = infoService.listInfoCount(catagoryType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mv.put( "catagoryType",catagoryType );
        mv.put( "list" ,list );
        return "/main/main";
    }
    @RequestMapping("/info")
    public @ResponseBody
    Map<String,Object> lists(Columns columns,int catagoryType) {
        Map<String,Object> map = new HashMap<>(  );
        PageHelper.startPage(columns.getDraw(),columns.getLength());
        List<Info> listinfo = infoService.listInfo(columns,catagoryType);
        PageInfo<Info> pageInfo = new PageInfo<Info>(listinfo);
        map.put( "data",pageInfo.getList() );
        map.put( "recordsTotal", pageInfo.getTotal() );
        map.put( "recordsFiltered",pageInfo.getTotal() );
        map.put( "draw",columns.getDraw() );
        return map;
    }




}
