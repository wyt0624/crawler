package com.surfilter.service;

import com.surfilter.entity.Info;

import java.util.List;

public interface INmapService {
    List<Info> listNmap();

    void updateListNmap(List<Info> list);
}
