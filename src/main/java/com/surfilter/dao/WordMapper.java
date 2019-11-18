package com.surfilter.dao;

import com.surfilter.dataobject.WordDO;
import com.surfilter.util.Keyword;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WordMapper {

    public void insertWord(@Param("wordList")List<String> wordDOList, @Param("wordType") int wordType);

    public List<Keyword> selectKeyWordByType(@Param("word_type")int type);

}
