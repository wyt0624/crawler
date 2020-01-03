package com.surfilter.dao;

import com.surfilter.entity.Phishing;
import com.surfilter.entity.WhiteUrl;

import java.util.List;

public interface WhiteUrlMapper {
    void addPhishing(List<Phishing> listphishing);

    List<WhiteUrl> listWhiteUrl();

    void insertWhiteUrl(List<WhiteUrl> list);
}
