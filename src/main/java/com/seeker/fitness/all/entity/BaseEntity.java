package com.seeker.fitness.all.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 实体父类，主要保存数据相关的通用字段
 */
public class BaseEntity {
    @JSONField(serialize=false)
    protected String addUser;//添加人
    @JSONField(serialize = false)
    protected String modifyUser;//最后修改人
    @JSONField(serialize = false)
    protected Date addDate;//添加时间
    @JSONField(serialize = false)
    protected Date modifyDate;//最后修改时间
    protected Integer valid;//数据是否有效 0-无效 1-有效

    public void info(){
        System.out.println("添加人:"+addUser);
        System.out.println("最后修改人:"+modifyUser);
        System.out.println("添加时间:"+addDate);
        System.out.println("最后修改时间:"+modifyDate);
        System.out.println("数据是否有效:"+valid);
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "addUser=" + addUser +
                ", modifyUser=" + modifyUser +
                ", addDate=" + addDate +
                ", modifyDate=" + modifyDate +
                ", valid=" + valid +
                '}';
    }

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
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

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }
}
