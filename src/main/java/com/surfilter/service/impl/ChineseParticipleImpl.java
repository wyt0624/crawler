package com.surfilter.service.impl;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.surfilter.service.ChineseParticiple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ChineseParticipleImpl implements ChineseParticiple {

    @Override
    public List<String> getChineseWord(String words) {
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<String> wordList = segmenter.sentenceProcess(words);
        return wordList;
    }



}
