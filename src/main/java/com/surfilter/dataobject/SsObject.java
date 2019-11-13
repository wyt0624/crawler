package com.surfilter.dataobject;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * author: wyt
 * 解析Json数组中的内容
 */
@Data
public class SsObject {

    private String json;

    private BigDecimal score;

    private Date startTime;

    private Long textNum;

    private Long imgNum;

}
