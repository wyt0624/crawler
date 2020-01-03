package com.surfilter.entity;

import lombok.Data;

/**钓鱼网站。
 */
@Data
public class Phishing {
    private Long id;
    private String url;
    private String domain;
}
