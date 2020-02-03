package com.surfilter.service.impl;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.surfilter.service.IWordService;
import com.surfilter.util.HttpUtil;
import com.surfilter.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class WordServiceImpl implements IWordService {

    @Override
    public Map<String,Object> getWords(String url) throws Exception {
        String title ="";
        Map<String,Object> map = new HashMap<>(  );

        Set<String> set =new HashSet<>(  );
        Document  doc = null;
        String newUrl = "";
        if (!url.startsWith("http://") && !url.startsWith("https://") ){
            newUrl = "http://"+url;
        } else {
            newUrl = url;
        }
        try {
            doc = HttpUtil.getDocByUrl( newUrl, 5000 );
        } catch (Exception e) {
            e.printStackTrace();
            log.info( "提取{}数据失败",newUrl);
            if (!url.startsWith("http://") || url.startsWith("https://") ){
                newUrl = "https://"+url;
                try {
                    doc = HttpUtil.getDocByUrl( newUrl, 5000 );
                } catch (IOException e1) {
                    log.info( "提取{}数据失败",newUrl  );
                }
            }
        }
        if( doc!= null ) {
            JiebaSegmenter segmenter = new JiebaSegmenter();
            title = doc.title();
            Elements eles = doc.getAllElements();
            for (Element ele:eles) {
                String text = ele.text();
                if(StringUtils.isNotBlank(text)){
                    List<String> list = segmenter.sentenceProcess(text );
                    for (String str:list){
                        String stt = StringUtil.RemoveSymbol(str);
                        if (StringUtils.isNotBlank(stt) && !StringUtil.isNumericzidai(stt)){
                            if (stt.length()>=2){
                                set.add( stt );
                            }
                        }
                    }
                }
            }
        }
        map.put( "url",newUrl );
        map.put( "record",set );
        map.put( "title", title );
        return map;
    }
}