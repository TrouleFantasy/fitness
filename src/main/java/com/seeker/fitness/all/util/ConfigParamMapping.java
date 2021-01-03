package com.seeker.fitness.all.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class ConfigParamMapping {
    private static String yamlMapString;
    private static Long tokenTimeOut;
    private static JSONObject yamlMap;

    static {
        Map<String,Object> yaml=ReadConfigFileUtil.getYaml();
        yamlMapString=JSONObject.toJSONString(yaml);
        yamlMap=JSONObject.parseObject(yamlMapString);
        tokenTimeOut=yamlMap.getJSONObject("user").getLong("tokenTimeOut");
    }
    public static JSONObject getYamlMap() {
        return JSONObject.parseObject(yamlMapString);
    }

    public static Long getTokenTimeOut() {
        return tokenTimeOut;
    }
}
