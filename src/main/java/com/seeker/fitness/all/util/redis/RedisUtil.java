package com.seeker.fitness.all.util.redis;

import com.seeker.fitness.all.config.ConfigParamMapping;
import com.seeker.fitness.all.util.PracticalUtil;
import com.seeker.fitness.all.util.Token;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public class RedisUtil{
    private static Long tokenTimeOut= ConfigParamMapping.getTokenTimeOut();
    /**
     * 判断给定token是否在有效时间内
     * @param token
     * @return
     */
    public static boolean isTokenTimeOut(String token,boolean isRenewal){
        Jedis jedis= RedisFactory.getJedis();
        try{
            if(StringUtils.isEmpty(token)){
                return false;
            }
            //获取userCode
            Token tokenObj=Token.parseTokenObj(token);
            String userCode=tokenObj.getUserCode();

            //获取payload中的用户在redis中的token数据
            Map<String,String> resultMap=jedis.hgetAll(userCode);
            Set<Map.Entry<String,String>> entrySet=resultMap.entrySet();
            for(Map.Entry<String,String> entry:entrySet){
                String key=entry.getKey();
                String value=entry.getValue();
                //毫秒级的 到期时间戳
                Long stopTime=Long.valueOf(value);
                //用户传入token与key比较 如果一致说明有此token
                if(token.equals(key)){
                    //对应的value就是到期时间戳 已转换为变量 stopTime
                    //如果stopTime大于当前时间戳 则说明token未过期 返回true
                    if(stopTime>new Date().getTime()){
                        //是否续期
                        if(isRenewal){
                            tokenRenewal(tokenObj);
                        }
                        return true;
                    }else{//否则则是过期 立即删除该token并且 默认返回false
                        jedis.hdel(userCode,key);
                    }
                }
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            jedis.close();
        }
    }

    //-------------------------------------------------------------------------------------------------------------------

    /**
     * 为给定token续期
     * @param token
     */
    public static void tokenRenewal(Token token){
        Jedis jedis= RedisFactory.getJedis();
        try {
            jedis.hset(token.getUserCode(),token.toTokenString(),String.valueOf(PracticalUtil.getTimeStamp(tokenTimeOut)));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
    }

    /**
     * 为给定token续期
     * @param tokenStr
     */
    public static void tokenRenewal(String tokenStr){
        Jedis jedis= RedisFactory.getJedis();
        try {
            Token token=Token.parseTokenObj(tokenStr);
            tokenRenewal(token);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
    }

    /**
     * 删除给定的用户相关登陆数据
     * @param userCode
     * @param token
     */
    public static void deleteToken(String userCode,String token){
        Jedis jedis=RedisFactory.getJedis();
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
            jedis.close();
        }
    }

    //-------------------------------------------------------------------------------------------------------------------
}
