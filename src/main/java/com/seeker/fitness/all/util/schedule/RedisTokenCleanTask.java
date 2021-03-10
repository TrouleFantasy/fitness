package com.seeker.fitness.all.util.schedule;

import com.seeker.fitness.all.config.ConfigParamMapping;
import com.seeker.fitness.all.util.redis.RedisFactory;
import com.seeker.fitness.all.util.redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TimerTask;

public class RedisTokenCleanTask extends TimerTask {
    private static Logger log= LoggerFactory.getLogger(RedisTokenCleanTask.class);
    @Override
    public void run() {
        Jedis jedis= RedisFactory.getJedis();
        try {
            log.info(">>>-----------------redis清理token计划任务开始("+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+")-----------------<<<");
            //拿到用户登陆记录集合信息
            Set<String> resultSet=jedis.smembers(ConfigParamMapping.getRedisLoginTableName());
            //循环查询对应用户登陆token hash表
            for(String userCode:resultSet){
                //清理给定用户不合法的token
                RedisUtil.violateToken(userCode,jedis);
            }
            log.info(">>>-----------------redis清理token计划任务结束("+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+")-----------------<<<");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
    }
}
