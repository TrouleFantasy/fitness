package com.seeker.fitness.all.mapper.fitnessmapper;

import com.seeker.fitness.all.entity.QueryTable;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface QueryTableMapper {
    /**
     * 获取所有允许查询详情的表
     * @return
     */
    @Result(property = "tableName",column = "table_name")
    @Select("SELECT * FROM query_table WHERE allow=1")
    List<QueryTable> queryAllowTable();

    /**
     * 获取所有允许查询详情的表名
     * @return
     */
    @Select("SELECT table_name FROM query_table WHERE allow=1")
    List<String> queryAllowTableName();

    /**
     * 获取所有Fitness所有表
     * @return
     */
    @Select("SELECT table_name FROM query_table")
    List<String> queryAllTableName();

    /**
     * 插入允许表
     * @return
     */
    @Insert("INSERT INTO query_table(id,table_name,allow) VALUES(#{id},#{tableName},#{allow})")
    Integer addAllowTable(QueryTable queryTable);
}
