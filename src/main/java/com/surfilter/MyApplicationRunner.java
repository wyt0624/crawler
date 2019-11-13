package com.surfilter;

import com.surfilter.dao.WordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class MyApplicationRunner implements ApplicationRunner {

    public static Set<String> filterWords = new HashSet<>();

    @Autowired
    private WordMapper wordMapper;

    public void loadWord() {

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
    }

}
