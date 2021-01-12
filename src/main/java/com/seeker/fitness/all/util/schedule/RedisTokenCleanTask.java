package com.seeker.fitness.all.util.schedule;

import com.seeker.fitness.all.config.ConfigParamMapping;
import com.seeker.fitness.all.util.redis.RedisFactory;
import com.seeker.fitness.all.util.redis.RedisUtil;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

public class RedisTokenCleanTask extends TimerTask {
    @Override
    public void run() {
        Jedis jedis= RedisFactory.getJedis();
        try {
            System.out.println(">>>---------------------------------------------------------------<<<");
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"运行redis清理过期token计划任务开始");
            //拿到用户登陆记录集合信息
            Set<String> resultSet=jedis.smembers(ConfigParamMapping.getRedisLoginTableName());
            //循环查询对应用户登陆token hash表
            for(String userCode:resultSet){
                //获取该用户所拥有的所有token信息
                Set<Map.Entry<String, String>> entrySet=jedis.hgetAll(userCode).entrySet();

                //循环对应的用户登录token hash表 进行token到期时间验证 清除已到期的token
                for (Map.Entry<String, String> entry:entrySet){
                    //key为token  value为到期时间戳(毫秒)
                    String key=entry.getKey();
                    String value=entry.getValue();
                    Long stopTime=Long.valueOf(value);
                    //如果当前时间大于过期时间 说明该token已失效 删除
                    if(new Date().getTime()>stopTime){
                        //调用RedisUtil工具类中的删除方法 当对应用户没有有效token时 会自动去删除对应的用户登录表中的数据
                        RedisUtil.deleteToken(userCode,key);
                        System.out.println("清理了"+userCode+"|"+key+":"+value);
                    }
                }
            }
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"运行redis清理过期token计划任务结束");
            System.out.println(">>>---------------------------------------------------------------<<<");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
    }
}
