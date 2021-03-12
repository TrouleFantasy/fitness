package com.seeker.fitness.all.util.schedule;

import com.seeker.fitness.all.config.ConfigParamMapping;
import com.seeker.fitness.all.entity.QueryTable;
import com.seeker.fitness.all.mapper.fitnessmapper.QueryTableMapper;
import com.seeker.fitness.all.mapper.informationschemamapper.TableMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProjectSchedule {
    private static Logger log= LoggerFactory.getLogger(ProjectSchedule.class);
    @Autowired
    private TableMapper tableMapper;
    @Autowired
    private QueryTableMapper queryTableMapper;
    public ProjectSchedule(){
        //定期清理redis中登录用户的缓存
        Timer timer=new Timer();
        timer.schedule(new RedisTokenCleanTask(),ConfigParamMapping.getScheduleStartCleanTokenTime(), ConfigParamMapping.getScheduleCleanTokenTime());

        //获取获取Fitness库所有表名 插入到允许访问记录表query_table中 启动项目立即执行一次
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.debug(">>>-----------------更新允许表计划任务开始("+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+")-----------------<<<");
                //获取Fitness库所有表名
                List<String> tableNames=tableMapper.getTableNameByDatabaseName("Fitness");
                List<String> allowTableNames=queryTableMapper.queryAllTableName();
                //删除已经存在的
                tableNames.removeAll(allowTableNames);
                //将剩下新增的表插入记录表中
                tableNames.forEach(tableName ->{queryTableMapper.addAllowTable(new QueryTable(tableName));});
                log.debug(">>>-----------------更新允许表计划任务结束("+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+")-----------------<<<");
            }
        },0);
    }
}
