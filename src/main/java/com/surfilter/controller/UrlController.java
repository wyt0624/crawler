package com.surfilter.controller;

import com.surfilter.error.BusinessException;
import com.surfilter.service.IWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//
@Controller
@RequestMapping("/url")
//@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class UrlController {

    @Autowired
    IWordService wordService;
    @RequestMapping(value = "/getWord")
    public @ResponseBody
    Map<String,Object> words(String url,HttpServletRequest request,
                             HttpServletResponse response) throws BusinessException {
        try {
            url= URLDecoder.decode(url, "UTF-8");//解码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String,Object> map = new HashMap<>(  );
        Set<String> set = null;
        try {
            map = wordService.getWords(url);
            map.put( "result",true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put( "result",false );
        }
        return map;
    }

//
//    @Autowired
//    private RedisRead redisRead;
//
//    @Autowired
//    private Word word;
//
//    @RequestMapping(value = "/getWord", method = {RequestMethod.POST})
//    @ResponseBody
//    public CommonReturnType getWord(@RequestParam(name = "url") String url) throws BusinessException {
//        Set<WordDO> set = redisRead.getWebMessageWord( url );
//        return CommonReturnType.create( set, 0, set.size() );
//    }
//
//
//    @RequestMapping(value = "/setWord", method = {RequestMethod.POST})
//    @ResponseBody
//    public CommonReturnType setWord(@RequestParam(name = "words") List<String> words,
//                                    @RequestParam(name = "word_type") int type) throws BusinessException {
//        word.insertWord( words, type );
//        return CommonReturnType.create( null );
//    }
//
//    @RequestMapping(value = "/webMagic", method = {RequestMethod.POST})
//    @ResponseBody
//    public CommonReturnType webMagic(@RequestParam(name = "url") String url) throws BusinessException {
//        Set<WordDO> set = redisRead.getWebMessageWord( url );
//        return CommonReturnType.create( set, 0, set.size() );
//    }
}
