package com.surfilter.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;


public class GsonUtil {
    public static String getJsonStringByObject(Set<String> domainSet) {
        return new Gson().toJson(domainSet);

    }
}
