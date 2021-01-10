package com.seeker.fitness.all.config;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.util.ReadConfigFileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ConfigParamMapping {
    private static String yamlMapString;
    private static Long tokenTimeOut;
    private static String secret;
    @Value("${user.tokenTimeOut}")
    private void setTokenTimeOut(Long tokenTimeOut) {
        ConfigParamMapping.tokenTimeOut = tokenTimeOut;
    }
    @Value("${user.secret}")
    private void setSecret(String secret) {
        ConfigParamMapping.secret = secret;
    }

    static {
        Map<String,Object> yaml= ReadConfigFileUtil.getYaml();
        yamlMapString=JSONObject.toJSONString(yaml);
    }

    public static Long getTokenTimeOut() {
        return tokenTimeOut;
    }

    public static String getSecret() {
        return secret;
    }

    public static JSONObject getYmlMap(){
        return JSONObject.parseObject(yamlMapString);
    }
}
