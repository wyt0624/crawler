package com.surfilter.controller;

import com.surfilter.dataobject.WordDO;
import com.surfilter.error.BusinessException;
import com.surfilter.response.CommonReturnType;
import com.surfilter.service.RedisRead;
import com.surfilter.service.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/url")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class UrlController {

    @Autowired
    private RedisRead redisRead;

    @Autowired
    private Word word;

    @RequestMapping(value = "/getWord", method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType getWord(@RequestParam(name = "url") String url) throws BusinessException {
        Set<WordDO> set = redisRead.getWebMessageWord( url );
        return CommonReturnType.create( set, 0, set.size() );
    }


    @RequestMapping(value = "/setWord", method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType setWord(@RequestParam(name = "words") List<String> words,
                                    @RequestParam(name = "word_type") int type) throws BusinessException {
        word.insertWord( words, type );
        return CommonReturnType.create( null );
    }

    @RequestMapping(value = "/webMagic", method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType webMagic(@RequestParam(name = "url") String url) throws BusinessException {
        Set<WordDO> set = redisRead.getWebMessageWord( url );
        return CommonReturnType.create( set, 0, set.size() );
    }
}
