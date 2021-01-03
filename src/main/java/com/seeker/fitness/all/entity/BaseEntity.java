package com.seeker.fitness.all.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 食物父类，主要保存四项日志
 */
public class BaseEntity {
    @JSONField(serialize=false)
    protected Integer addUser;//'添加人',
    @JSONField(serialize = false)
    protected Integer modifyUser;//'最后修改人',
    @JSONField(serialize = false)
    protected Date addDate;//'添加时间',
    @JSONField(serialize = false)
    protected Date modifyDate;//'最后修改时间'

    public void info(){
        System.out.println("添加人:"+addUser);
        System.out.println("最后修改人:"+modifyUser);
        System.out.println("添加时间:"+addDate);
        System.out.println("最后修改时间:"+modifyDate);
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "addUser=" + addUser +
                ", modifyUser=" + modifyUser +
                ", addDate=" + addDate +
                ", modifyDate=" + modifyDate +
                '}';
    }

    public Integer getAddUser() {
        return addUser;
    }

    public void setAddUser(Integer addUser) {
        this.addUser = addUser;
    }

    public Integer getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(Integer modifyUser) {
        this.modifyUser = modifyUser;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}
