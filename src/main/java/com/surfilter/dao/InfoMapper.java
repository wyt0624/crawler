package com.surfilter.dao;

import com.surfilter.entity.Info;

import java.util.List;
import java.util.Map;

public interface InfoMapper {
    void addInfo(Info info);

    void addListInfo(List<Info> listInfo);

    List<Info> listRuleInfo();

    void updateRuleInfo(List<Info> list);

    List<Info> listInfo(Map<String, Object> content);

    List<Info> listInfo1();

    void updateInfo(List<Info> listinfo);

    List<Info> listInfoCount(Map<String, Object> catagoryType);
}
