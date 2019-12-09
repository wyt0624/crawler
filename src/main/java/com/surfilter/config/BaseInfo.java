package com.surfilter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "base.info")
public class BaseInfo {
    private String urlReadPath; //url读取目录
   // private String urlSnapshot; //快照保存目录。
    private String sysSole;

}
