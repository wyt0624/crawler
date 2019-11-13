package com.surfilter.service;

import com.surfilter.dataobject.WordDO;

import java.util.List;

public interface Word {

    /**
     * 单词分类入库
     * @param word
     * @param type
     */
    public void insertWord(List<String> word, int type);
}
