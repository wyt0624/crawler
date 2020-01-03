package com.surfilter.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.surfilter.entity.Info;
import com.surfilter.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class indexController {
    @Autowired
    InfoService infoService;

    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView( "/index" );
        mv.addObject( "name", "hello,world!" );
        return mv;
    }

    @RequestMapping("/info")
    public @ResponseBody PageInfo<Info> lists(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        PageInfo<Info> pageInfo = new PageInfo<Info>(infoService.listInfo());
        return pageInfo;
    }


}
