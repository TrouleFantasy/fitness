package com.seeker.fitness.all.util.redis;

import com.seeker.fitness.all.util.ConfigParamMapping;
import com.seeker.fitness.all.util.PracticalUtil;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

public class RedisUtil{
    private static Long tokenTimeOut= ConfigParamMapping.getTokenTimeOut();
    /**
     * 判断给定token是否有效
     * @param token
     * @return
     */
    public static boolean isTokenValid(String token){
        if(StringUtils.isEmpty(token)){
            return false;
        }
        Jedis jedis= RedisFactory.getJedis();
        String result=jedis.get(token);
        jedis.close();
        if(!StringUtils.isEmpty(result)&&!"null".equals(result)){
            return true;
        }
        return false;
    }

    //-------------------------------------------------------------------------------------------------------------------

    /**
     * 为给定token续期
     * @param token
     */
    public static void tokenRenewal(String token){
        Jedis jedis= RedisFactory.getJedis();
        jedis.expireAt(token, PracticalUtil.getTimeStamp(tokenTimeOut));
        jedis.close();
    }

    //-------------------------------------------------------------------------------------------------------------------

}
