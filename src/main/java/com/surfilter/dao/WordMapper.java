package com.surfilter.dao;

import com.surfilter.util.Keyword;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface WordMapper {

    public void insertWord(@Param("wordList") List<String> wordDOList, @Param("wordType") int wordType);

    public List<Keyword> selectKeyWordByType(@Param("word_type")int type);

}
