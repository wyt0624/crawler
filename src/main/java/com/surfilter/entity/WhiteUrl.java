package com.surfilter.entity;

import lombok.Data;

/***
 *
 * 白名单bean
 */
@Data
public class WhiteUrl {
    private Long id;
    private String url;
    private String name;
}
