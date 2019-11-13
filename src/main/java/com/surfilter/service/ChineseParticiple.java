package com.surfilter.service;


import java.util.List;

public interface ChineseParticiple {
    /**
     * 中文分词
     * @param words
     * @return
     */
    List<String> getChineseWord(String words);
}
