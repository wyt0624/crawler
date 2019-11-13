package com.surfilter.service.impl;

import com.surfilter.dao.WordMapper;
import com.surfilter.dataobject.WordDO;
import com.surfilter.service.Word;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WordImpl implements Word {

    @Autowired
    private WordMapper wordMapper;



    @Override
    public void insertWord(List<String> wordList, int type) {
        wordMapper.insertWord(wordList,type);
    }
}
