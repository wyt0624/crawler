package com.surfilter.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.surfilter.entity.Columns;
import com.surfilter.entity.Info;
import com.surfilter.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class indexController {
    @Autowired
    InfoService infoService;

    @RequestMapping("/index")
    public ModelAndView index1(@RequestParam(value="category",required=false) Integer catagoryType) {
        ModelAndView mv = new ModelAndView( "/index" );
        List<Info> list = null;
        try {
            list = infoService.listInfoCount(catagoryType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (catagoryType == null){
            mv.addObject( "catagoryType",0 );
        } else {
            mv.addObject( "catagoryType",catagoryType );
        }

        mv.addObject( "list" ,list );
        return mv;
    }
    @RequestMapping("/info")
    public @ResponseBody
    Map<String,Object> lists(Columns columns,int catagoryType) {
        Map<String,Object> map = new HashMap<>(  );
        PageHelper.startPage(columns.getStart(),columns.getLength());
        PageInfo<Info> pageInfo = new PageInfo<Info>(infoService.listInfo(columns,catagoryType));
       // return pageInfo;
        map.put( "data",pageInfo.getList() );
        map.put( "recordsTotal", pageInfo.getTotal() );
        map.put( "recordsFiltered",pageInfo.getTotal() );
        map.put( "draw",columns.getDraw() );
        return map;
    }




}
