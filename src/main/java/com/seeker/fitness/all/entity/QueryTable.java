package com.seeker.fitness.all.entity;

public class QueryTable {
    private Integer id;
    private String tableName;
    private Integer allow=0;

    public QueryTable(){}
    public QueryTable(String tableName){
        this.tableName=tableName;
    }
    public QueryTable(String tableName,Integer allow){
        this.tableName=tableName;
        this.allow=allow;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getAllow() {
        return allow;
    }

    public void setAllow(Integer allow) {
        this.allow = allow;
    }
    @Override
    public String toString() {
        return "QueryTable{" +
                "id=" + id +
                ", tableName='" + tableName + '\'' +
                ", allow=" + allow +
                '}';
    }
}
