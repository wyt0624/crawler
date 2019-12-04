package com.surfilter.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AnaliseInfo implements Serializable {
    private String html;
    private String url;
    private String title;
}
