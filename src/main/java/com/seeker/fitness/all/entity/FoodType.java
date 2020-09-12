package com.seeker.fitness.all.entity;

import java.util.Objects;

public class FoodType extends BaseEntity{
    private Integer ftid ;//id
    private Integer type;//种类代码
    private String chType;//种类名称
    private Integer upType;//上级种类
    private String typeDescribe;//种类描述

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodType foodType = (FoodType) o;
        return Objects.equals(ftid, foodType.ftid) &&
                Objects.equals(type, foodType.type) &&
                Objects.equals(chType, foodType.chType) &&
                Objects.equals(upType, foodType.upType) &&
                Objects.equals(typeDescribe, foodType.typeDescribe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ftid, type, chType, upType, typeDescribe);
    }

    @Override
    public String toString() {
        return "FoodType{" +
                "ftid=" + ftid +
                ", type=" + type +
                ", chType='" + chType + '\'' +
                ", upType=" + upType +
                ", typeDescribe='" + typeDescribe + '\'' +
                "} " + super.toString();
    }

    public Integer getFtid() {
        return ftid;
    }

    public void setFtid(Integer ftid) {
        this.ftid = ftid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getChType() {
        return chType;
    }

    public void setChType(String chType) {
        this.chType = chType;
    }

    public Integer getUpType() {
        return upType;
    }

    public void setUpType(Integer upType) {
        this.upType = upType;
    }

    public String getTypeDescribe() {
        return typeDescribe;
    }

    public void setTypeDescribe(String typeDescribe) {
        this.typeDescribe = typeDescribe;
    }
}
