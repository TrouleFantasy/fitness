package com.seeker.fitness.all.mapper.informationschemamapper;

import com.seeker.fitness.all.entity.ColumnInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TableMapper {
    /**
     * 获取Fitness.user_list表详情
     * @param tableName
     * @return
     */
    @Select("select * from COLUMNS where table_name = #{tableName} and TABLE_SCHEMA = 'Fitness'")
    List<ColumnInfo> getTableInfo(String tableName);

    /**
     * 获取指定库所有表详情
     * @param databaseName
     * @return
     */
    @Select("select * from COLUMNS where TABLE_SCHEMA = #{databaseName}")
    List<ColumnInfo> getTableInfoByDatabaseName(String databaseName);

    /**
     * 获取指定库所有表名
     * @param databaseName
     * @return
     */
    @Select("select distinct TABLE_NAME from COLUMNS where TABLE_SCHEMA = #{databaseName}")
    List<String> getTableNameByDatabaseName(String databaseName);
}
