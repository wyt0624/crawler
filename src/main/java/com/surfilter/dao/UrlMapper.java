package com.surfilter.dao;

import com.surfilter.entity.DomainUrl;

import java.util.List;

public interface UrlMapper {
    void addDomainUrl(List<DomainUrl> listUrl);
}
