package com.seeker.fitness.all.config;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.util.ReadConfigFileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Configuration
public class ConfigParamMapping {
    private static String yamlMapString;
    //毫秒为单位
    private static Long tokenTimeOut;
    private static Long scheduleCleanTokenTime;
    private static String secret;
    private static String redisLoginTableName;
    @Value("${user.tokenTimeOut}")
    private void setTokenTimeOut(String tokenTimeOutStr) {
        ConfigParamMapping.tokenTimeOut = parseTimeLong(tokenTimeOutStr);
    }
    @Value("${user.scheduleCleanTokenTime}")
    private void setScheduleCleanTokenTime(String scheduleCleanTokenTimeStr) {
        ConfigParamMapping.scheduleCleanTokenTime = parseTimeLong(scheduleCleanTokenTimeStr);
    }

    @Value("${user.secret}")
    private void setSecret(String secret) {
        ConfigParamMapping.secret = secret;
    }
    @Value("${user.redisLoginTableName}")
    private void setRedisLoginTableName(String redisLoginTableName) {
        ConfigParamMapping.redisLoginTableName = redisLoginTableName;
    }

    static {
        Map<String,Object> yaml= ReadConfigFileUtil.getYaml();
        yamlMapString=JSONObject.toJSONString(yaml);
    }

    public static Long getTokenTimeOut() {
        return tokenTimeOut;
    }

    public static Long getScheduleCleanTokenTime() {
        return scheduleCleanTokenTime;
    }

    public static String getSecret() {
        return secret;
    }

    public static String getRedisLoginTableName() {
        return redisLoginTableName;
    }
    //--------------------------------------------------------------------------
    public static JSONObject getYmlMap(){
        return JSONObject.parseObject(yamlMapString);
    }

    /**
     * 根据传入类型返回对应的值 本方法可传入父子关系key 将会去循环向下调用
     * @param clazz
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getParam(Class<T> clazz, String ... key){
        JSONObject yamlMap=JSONObject.parseObject(yamlMapString);
        for(int i=0;i<key.length-1;i++){
            yamlMap=yamlMap.getJSONObject(key[i]);
        }
        Object param=yamlMap.get(key[key.length-1]);
        return clazz.cast(param);
    }
    //--------------------------------------------------------------------------
    /**
     * 将指定带单位的字符串数值转换为Long类型
     * @param timeStr
     * @return
     */
    private static Long parseTimeLong(String timeStr){
        //默认5分钟
        Long timeLong=300000L;
        //不为空才进行赋值操作否则沿用默认数值
        if(timeStr!=null&&!"".equals(timeStr)){
            String timeStrLower=timeStr.toLowerCase();
            //此map记录了不同单位与毫秒的进制关系 key为单位 value为进制
            Map<String,Integer> map=new HashMap<>();
            map.put("ms",1);
            map.put("s",1000);

            Set<Map.Entry<String,Integer>> set=map.entrySet();
            for(Map.Entry<String,Integer> entry:set){
                String key=entry.getKey();
                Integer value=entry.getValue();
                //正则表达式
                String regex="^[0-9]*"+key+"$";
                //判断此次循环的数值是否是所支持的单位
                if(timeStrLower.matches(regex)){
                    //如果是是直接去掉单位 转换为数值
                    timeLong=Long.valueOf(timeStrLower.replaceAll("[^0-9]",""));
                    //转换为毫秒
                    return timeLong*=value;
                }
            }
        }
        return timeLong;
    }
}
