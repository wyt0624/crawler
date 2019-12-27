package com.surfilter.dao;

import com.surfilter.entity.CountryInfo;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Map;

public interface CountryMapper {
    void insertCountry(List<CountryInfo> list);

    int getCount(@Param("en") String value);

    void updateCountryCaptical(Map<String, String> map);

    void insertCountryInfo(Map<String, String> map);
}
