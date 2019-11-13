package com.surfilter.dao;

import com.surfilter.dataobject.WordDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WordMapper {

    public void insertWord(@Param("wordList")List<String> wordDOList, @Param("wordType") int wordType);

}
