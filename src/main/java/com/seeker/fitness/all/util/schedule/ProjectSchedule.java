package com.seeker.fitness.all.util.schedule;

import com.seeker.fitness.all.config.ConfigParamMapping;

import java.util.Timer;

public class ProjectSchedule {

    public ProjectSchedule(){
        //定期清理redis中登录用户的缓存
        Timer timer=new Timer();
        timer.schedule(new RedisTokenCleanTask(),ConfigParamMapping.getScheduleStartCleanTokenTime(), ConfigParamMapping.getScheduleCleanTokenTime());

    }
}
