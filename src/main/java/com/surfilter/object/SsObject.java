package com.surfilter.object;

import java.math.BigDecimal;
import java.util.Date;

/**
 * author: wyt
 * 解析Json数组中的内容
 */
public class SsObject {

    private String json;

    private BigDecimal score;

    private Date startTime;

    private Long textNum;

    private Long imgNum;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Long getTextNum() {
        return textNum;
    }

    public void setTextNum(Long textNum) {
        this.textNum = textNum;
    }

    public Long getImgNum() {
        return imgNum;
    }

    public void setImgNum(Long imgNum) {
        this.imgNum = imgNum;
    }
}
