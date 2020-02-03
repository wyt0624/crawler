package com.surfilter.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.surfilter.entity.*;
import com.surfilter.service.ITemplateService;
import com.surfilter.service.IWhiteListService;
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
    @Autowired
    ITemplateService templateService;
    @Autowired
    IWhiteListService whiteListService;

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
        PageHelper.startPage((columns.getStart()/columns.getLength() ) +1,columns.getLength());
        List<Info> listinfo = infoService.listInfo(columns,catagoryType);
        PageInfo<Info> pageInfo = new PageInfo<Info>(listinfo);
        map.put( "data",pageInfo.getList() );
        map.put( "recordsTotal", pageInfo.getTotal() );
        map.put( "recordsFiltered",pageInfo.getTotal() );
        map.put( "draw",columns.getDraw() );
        return map;
    }
    @RequestMapping("/whitelist")
    public @ResponseBody
    Map<String,Object> whiteList(Columns columns) {
        Map<String,Object> map = new HashMap<>(  );
        PageHelper.startPage((columns.getStart()/columns.getLength() ) +1,columns.getLength());
        List<WhiteUrl> listinfo = whiteListService.listWhite(columns);
        PageInfo<WhiteUrl> pageInfo = new PageInfo<WhiteUrl>(listinfo);
        map.put( "data",pageInfo.getList() );
        map.put( "recordsTotal", pageInfo.getTotal() );
        map.put( "recordsFiltered",pageInfo.getTotal() );
        map.put( "draw",columns.getDraw() );
        return map;
    }
    @RequestMapping("/blacklist")
    public @ResponseBody
    Map<String,Object> blacklist(Columns columns) {
        Map<String,Object> map = new HashMap<>(  );
        PageHelper.startPage((columns.getStart()/columns.getLength() ) +1,columns.getLength());
        List<Phishing> listinfo = whiteListService.listBlack(columns);
        PageInfo<Phishing> pageInfo = new PageInfo<Phishing>(listinfo);
        map.put( "data",pageInfo.getList() );
        map.put( "recordsTotal", pageInfo.getTotal() );
        map.put( "recordsFiltered",pageInfo.getTotal() );
        map.put( "draw",columns.getDraw() );
        return map;
    }

    @RequestMapping("/template")
    public @ResponseBody
    Map<String,Object> templatelist(Columns columns) {
        Map<String,Object> map = new HashMap<>(  );
        PageHelper.startPage((columns.getStart()/columns.getLength() ) +1,columns.getLength());
        List<Template> listinfo = templateService.listTemplate(columns);
        PageInfo<Template> pageInfo = new PageInfo<Template>(listinfo);
        map.put( "data",pageInfo.getList() );
        map.put( "recordsTotal", pageInfo.getTotal() );
        map.put( "recordsFiltered",pageInfo.getTotal() );
        map.put( "draw",columns.getDraw() );
        return map;
    }
    @RequestMapping("opt/template")
    public @ResponseBody
    Map<String,Object> addtem(Template template) {
        Map<String,Object> map = new HashMap<>(  );
        try {
            map =  templateService.optTemplate(template);
            map.put( "result",true );
        } catch (Exception e) {
            map.put( "result",false );
            e.printStackTrace();
        }
        return map;
    }
    @RequestMapping("check/url")
    public @ResponseBody
    Map<String,Object> chekUrl(Template template) {
        Map<String,Object> map = new HashMap<>(  );
        try {
            map =  templateService.checkUrl(template);
            map.put( "result",true );
        } catch (Exception e) {
            map.put( "result",false );
            e.printStackTrace();
        }
        return map;
    }
    @RequestMapping("del/template")
    public @ResponseBody
    Map<String,Object> delTemplate(Template template) {
        Map<String,Object> map = new HashMap<>(  );
        try {
            map =  templateService.delTemplate(template);
            map.put( "result",true );
        } catch (Exception e) {
            map.put( "result",false );
            e.printStackTrace();
        }
        return map;
    }




}
