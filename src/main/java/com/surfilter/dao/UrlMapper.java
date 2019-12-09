package com.surfilter.dao;

import com.surfilter.entity.DomainUrl;

import java.util.List;

public interface UrlMapper {
    void addDomainUrl(List<DomainUrl> listUrl);
    List<DomainUrl> listCrawler();
    void updateListCrawler(List<DomainUrl> list);
    List<DomainUrl> listCache();
    void updateListCache(List<DomainUrl> list);

    void updateListCrawlerStatus();
}
