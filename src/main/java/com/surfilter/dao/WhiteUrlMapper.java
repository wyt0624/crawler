package com.surfilter.dao;

import com.surfilter.entity.Phishing;
import com.surfilter.entity.WhiteUrl;

import java.util.List;
import java.util.Map;

public interface WhiteUrlMapper {
    void addPhishing(List<Phishing> listphishing);

    List<WhiteUrl> listWhiteUrl();

    void insertWhiteUrl(List<WhiteUrl> list);

    List<WhiteUrl> listWhite(Map<String, Object> param);

    List<Phishing> listBlack(Map<String, Object> param);
}
