package com.surfilter.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class domain {
    public static void main(String[] args) throws IOException {
        List<Keyword> keywords = new ArrayList<Keyword>();
        Set<Keyword> result = new HashSet<Keyword>();
        Keyword a1= new Keyword();
        a1.setWord("手机");
        keywords.add(a1);

        Keyword a3= new Keyword();
        a3.setWord("数码");
        keywords.add(a3);
        Patterns patterns=new Patterns(keywords);
        Document doc = Jsoup.connect("https://www.jd.com")
                .data("query", "Java")
                .userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(3000)
                .post();

        System.out.println(doc.html());
        result=patterns.searchKeyword(doc.html(), null);
        System.out.println("keys: ");
        for(Keyword key:result){
            System.out.println(key.getWord());
        }
    }

}


