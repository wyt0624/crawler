package com.surfilter;

import com.surfilter.dao.WordMapper;
import com.surfilter.enums.Param;
import com.surfilter.util.Keyword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class MyApplicationRunner implements ApplicationRunner {

    public static Set<String> filterWords = new HashSet<>();

    public static List<Keyword> vpnWordList = new ArrayList<>();
    public static List<Keyword> yellowWordList = new ArrayList<>();
    public static List<Keyword> wadingListList = new ArrayList<>();
    public static List<Keyword> whiteWordListList = new ArrayList<>();
    @Autowired
    private WordMapper wordMapper;

    /**
     * 加载词语
     */
    public void loadWord() {
        vpnWordList = wordMapper.selectKeyWordByType(Param.VPN_WORD.getCode());
        yellowWordList = wordMapper.selectKeyWordByType(Param.YELLOW_WORD.getCode());
        wadingListList = wordMapper.selectKeyWordByType(Param.WADING_WORD.getCode());
        whiteWordListList = wordMapper.selectKeyWordByType(Param.WHITE_WORD.getCode());
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadWord();
    }

}
