package com.surfilter.entity;

import lombok.Data;

/**
 * 国家信息表。
 */
@Data
public class CountryInfo {
    private long id;//id
    private String countryCn;//国家中文
    private String countryEn;//国际英文
    private String province;//省份
    private String lngX;//x
    private String lngY;//y
    private String capital;//首都
}
