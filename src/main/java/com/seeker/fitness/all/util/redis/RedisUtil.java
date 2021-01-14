package com.seeker.fitness.all.util.redis;

import com.seeker.fitness.all.config.ConfigParamMapping;
import com.seeker.fitness.all.util.PracticalUtil;
import com.seeker.fitness.all.util.Token;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.*;

public class RedisUtil{
    private static Long tokenTimeOut= ConfigParamMapping.getTokenTimeOut();
    /**
     * 判断给定token是否有效
     * @param token
     * @return
     */
    public static boolean isTokenTimeOut(String token,boolean isRenewal,Jedis ...jedisArr){
        Jedis jedis;
        boolean isDown=false;
        if(jedisArr.length>0){
            jedis=jedisArr[0];
        }else{
            isDown=true;
            jedis= RedisFactory.getJedis();
        }
        try{
            if(StringUtils.isEmpty(token)){
                return false;
            }
            //获取userCode
            Token tokenObj=Token.parseTokenObj(token);
            String userCode=tokenObj.getUserCode();

            //首先判断此用户是否已经登陆
            if(jedis.sismember(ConfigParamMapping.getRedisLoginTableName(),userCode)){
                //如果已经登陆则去判断该用户是否拥有此token
                if(jedis.hexists(userCode,token)){
                    //如果有则获取此token信息
                    String tokenInfo=jedis.hget(userCode,token);
                    //毫秒级的 到期时间戳
                    Long stopTime=0L;
                    try{
                        stopTime=Long.valueOf(tokenInfo.split(ConfigParamMapping.getTokenSpaceMark())[0]);
                    }catch (NumberFormatException n){
                        System.out.println("RedisUtil.isTokenTimeOut()>>>出现了数字转换异常，可能是redis中token到期时间格式不正确。");
                    }
                    //如果stopTime大于当前时间戳 则说明token未过期 返回true
                    if(stopTime>new Date().getTime()){
                        //是否续期
                        if(isRenewal){
                            tokenRenewal(tokenObj);
                        }
                        return true;
                    }
                }
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            if(isDown){
                jedis.close();
            }
        }
    }

    //-------------------------------------------------------------------------------------------------------------------

    /**
     * 为给定token续期
     * @param token
     */
    public static void tokenRenewal(Token token,Jedis ...jedisArr){
        Jedis jedis;
        boolean isDown=false;
        if(jedisArr.length>0){
            jedis=jedisArr[0];
        }else{
            isDown=true;
            jedis= RedisFactory.getJedis();
        }
        try {
            //间隔符号
            String spaceMark=ConfigParamMapping.getTokenSpaceMark();
            String[] userAgentInfo=jedis.hget(token.getUserCode(),token.toTokenString()).split(spaceMark);
            userAgentInfo[0]=String.valueOf(PracticalUtil.getTimeStamp(tokenTimeOut));
            String newUserAgentInfo=PracticalUtil.arrayToString(userAgentInfo,spaceMark);
            jedis.hset(token.getUserCode(),token.toTokenString(),newUserAgentInfo);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(isDown){
                jedis.close();
            }
        }
    }

    /**
     * 为给定token续期
     * @param tokenStr
     */
    public static void tokenRenewal(String tokenStr,Jedis ...jedisArr){
        try {
            Token token=Token.parseTokenObj(tokenStr);
            tokenRenewal(token,jedisArr);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取给定用户最先到期的token
     * @param userCode
     * @return
     */
    public static String getRecentToken(String userCode,Jedis ...jedisArr){
        Jedis jedis;
        boolean isDown=false;
        if(jedisArr.length>0){
            jedis=jedisArr[0];
        }else{
            isDown=true;
            jedis= RedisFactory.getJedis();
        }
        try {
            //获取给定用户所有token
            Map<String, String> userTokenInfo=jedis.hgetAll(userCode);
            //创建一个List接收排序结果
            List<Map.Entry<String,String>> sortList=sortTokenList(userTokenInfo);
            //将第一个元素 也就是最早到期的token返回
            return sortList.get(0).getKey();
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }finally {
            if(isDown){
                jedis.close();
            }
        }
    }
    /**
     * 获取给定用户最晚到期的token
     * @param userCode
     * @return
     */
    public static String getLaterToken(String userCode,Jedis ...jedisArr){
        Jedis jedis;
        boolean isDown=false;
        if(jedisArr.length>0){
            jedis=jedisArr[0];
            isDown=true;
        }else{
            jedis= RedisFactory.getJedis();
        }
        try {
            //获取给定用户所有token
            Map<String, String> userTokenInfo=jedis.hgetAll(userCode);
            //创建一个List接收排序结果
            List<Map.Entry<String,String>> sortList=sortTokenList(userTokenInfo);
            //将第最后一个元素 也就是最晚到期的token返回
            return sortList.get(sortList.size()-1).getKey();
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }finally {
            if(isDown){
                jedis.close();
            }
        }
    }

    /**
     * 为传入的指定token排序 约早到期下标越靠前
     * @param tokenMap
     * @return
     */
    public static List<Map.Entry<String,String>> sortTokenList(Map<String, String> tokenMap){
        //创建一个List以便排序
        List<Map.Entry<String,String>> sortList=new ArrayList<>();
        sortList.addAll(tokenMap.entrySet());
        //开始排序
        Collections.sort(sortList,(o1,o2)->{
                    String v1=PracticalUtil.headGetStringByRegex(".*?"+ConfigParamMapping.getTokenSpaceMark(),o1.getValue(),false);
                    String v2=PracticalUtil.headGetStringByRegex(".*?"+ConfigParamMapping.getTokenSpaceMark(),o2.getValue(),false);
                    Long ov1=Long.valueOf(v1.substring(0,v1.indexOf(ConfigParamMapping.getTokenSpaceMark())));
                    Long ov2=Long.valueOf(v2.substring(0,v2.indexOf(ConfigParamMapping.getTokenSpaceMark())));
                    Long result=ov1-ov2;
                    return result==0?0:result>0?1:-1;
                });
        return sortList;
    }

    /**
     * 清理不合法的token
     * @param userCode
     */
    public static void violateToken(String userCode,Jedis ...jedisArr){
        Jedis jedis;
        boolean isDown=false;
        if(jedisArr.length>0){
            jedis=jedisArr[0];
        }else{
            isDown=true;
            jedis= RedisFactory.getJedis();
        }
        try {
            //1.删除已到期token
            //获取给定用户的所有token
            Map<String, String> tokenMap=jedis.hgetAll(userCode);
            for(Map.Entry<String,String> entry:tokenMap.entrySet()){
                String token=entry.getKey();
                String value=entry.getValue();
                //判断token是否到期
                if(Long.valueOf(value.split(ConfigParamMapping.getTokenSpaceMark())[0])<new Date().getTime()){
                    //如果已到期则删除对应token
                    deleteToken(userCode,token,jedis);
                    System.out.println("超时|被删除token信息:<<"+userCode+":"+token+":"+value+">>");
                }
            }

            //2.删除多余token
            //如果已有token数量减去最大允许的token数量还大于0的话 则说明有多余token 按照到期顺序删除多余的token
            List<Map.Entry<String,String>> sortList=sortTokenList(tokenMap);//排序
            for(int i=0;i<sortList.size()-ConfigParamMapping.getTokenTotal();i++){
                Map.Entry<String,String> entry=sortList.get(i);
                String token=entry.getKey();
                String value=entry.getValue();
                deleteToken(userCode,token,jedis);
                System.out.println("超量|被删除token信息:<<"+userCode+":"+token+":"+value+">>");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(isDown){
                jedis.close();
            }
        }
    }

    /**
     * 删除给定的用户的指定tonken
     * @param userCode
     * @param token
     */
    public static void deleteToken(String userCode,String token,Jedis ...jedisArr){
        Jedis jedis;
        boolean isDown=false;
        if(jedisArr.length>0){
            jedis=jedisArr[0];
        }else{
            isDown=true;
            jedis= RedisFactory.getJedis();
        }
        try {
            //删除指定token
            jedis.hdel(userCode,token);
            //删除后查询对应用户记录token的hash表长度
            Long hlen=jedis.hlen(userCode);
            //如果该表长度小于等于0 说明该用户已经没有在登录状态的设备了 届时删除用户登陆集合表中对于该用户的记录
            if(hlen<=0){
                jedis.srem(ConfigParamMapping.getRedisLoginTableName(),userCode);
                System.out.println("用户"+userCode+"已无有效token，已清空"+ConfigParamMapping.getRedisLoginTableName()+"中对应记录");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(isDown){
                jedis.close();
            }
        }
    }

    /**
     * 删除指定用户的所有token
     * @param userCode
     */
    public static void cleanAllToken(String userCode,Jedis ...jedisArr){
        Jedis jedis;
        boolean isDown=false;
        if(jedisArr.length>0){
            jedis=jedisArr[0];
        }else{
            isDown=true;
            jedis= RedisFactory.getJedis();
        }
        try{
            //直接删除对应用户token表
            jedis.del(userCode);
            //之后再移除对应用户的登陆记录表信息
            jedis.srem(ConfigParamMapping.getRedisLoginTableName(),userCode);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(isDown){
                jedis.close();
            }
        }
    }

    //-------------------------------------------------------------------------------------------------------------------
}
