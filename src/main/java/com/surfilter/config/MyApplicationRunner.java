package com.surfilter.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyApplicationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

//    public static Set<String> filterWords = new HashSet<>();
//
//    public static List<Keyword> vpnWordList = new ArrayList<>();
//    public static List<Keyword> yellowWordList = new ArrayList<>();
//    public static List<Keyword> wadingListList = new ArrayList<>();
//    public static List<Keyword> whiteWordListList = new ArrayList<>();
//
//
//    public static String writeFilepath;
//    public static List<UrlDO> whiteUrlList = new ArrayList<>();

//    @Autowired
//    private WordMapper wordMapper;

//    @Autowired
//    private UrlMapper urlMapper;

//    private String readRedisPath;
//
//    @Value("${job.param.read-redis-path}")
//    public  String readToRedisPath;
//
//    @Value("${job.param.write-file-path}")
//    public String writeFilePath;
//
//    @Value("${job.param.yes-file-path}")
//    public String writeYesFilePath;


    /**
     * 加载词语
     */
//    public void loadWord() {
//        writeFilepath = writeFilePath;
//        vpnWordList = wordMapper.selectKeyWordByType(Param.VPN_WORD.getCode());
//        yellowWordList = wordMapper.selectKeyWordByType(Param.YELLOW_WORD.getCode());
//        wadingListList = wordMapper.selectKeyWordByType(Param.WADING_WORD.getCode());
//        whiteWordListList = wordMapper.selectKeyWordByType(Param.WHITE_WORD.getCode());
//        //whiteUrlList = urlMapper.selectUrlListByType(Param.WHITE_URL.getCode());
//    }

    /**
     * 创建需要的目录
     */
//    public void createDir() {
//        FileUtil.isDirctionary(readToRedisPath);
//        FileUtil.isDirctionary(writeFilePath);
//        FileUtil.isDirctionary(writeYesFilePath);
//    }

//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        loadWord();
//        createDir();
//    }

}
