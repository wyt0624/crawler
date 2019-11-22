package com.surfilter;

import com.surfilter.dao.UrlMapper;
import com.surfilter.dao.WordMapper;
import com.surfilter.dataobject.UrlDO;
import com.surfilter.enums.Param;
import com.surfilter.job.ScheduledTasks;
import com.surfilter.util.FileUtil;
import com.surfilter.util.Keyword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
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

    public static List<UrlDO> whiteUrlList = new ArrayList<>();

    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private UrlMapper urlMapper;

    private String readRedisPath;

    @Value("${job.param.read-redis-path}")
    private String readToRedisPath;

    @Value("${job.param.write-file-path}")
    private String writeFilePath;

    @Value("${job.param.yes-file-path}")
    private String writeYesFilePath;


    /**
     * 加载词语
     */
    public void loadWord() {
        vpnWordList = wordMapper.selectKeyWordByType(Param.VPN_WORD.getCode());
        yellowWordList = wordMapper.selectKeyWordByType(Param.YELLOW_WORD.getCode());
        wadingListList = wordMapper.selectKeyWordByType(Param.WADING_WORD.getCode());
        whiteWordListList = wordMapper.selectKeyWordByType(Param.WHITE_WORD.getCode());
        whiteUrlList = urlMapper.selectUrlListByType(Param.WHITE_URL.getCode());
    }

    /**
     * 创建需要的目录
     */
    public void createDir() {
        FileUtil.isDirctionary(readToRedisPath);
        FileUtil.isDirctionary(writeFilePath);
        FileUtil.isDirctionary(writeYesFilePath);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadWord();
        createDir()
    }

}
