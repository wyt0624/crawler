package com.surfilter.dao;

import com.surfilter.entity.Info;

import java.util.List;

public interface NmapMapper {

    List<Info> listNmap();

    void updateListNmap(List<Info> list);
}
