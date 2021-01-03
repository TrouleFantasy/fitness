package com.seeker.fitness.all.entity;

import java.util.Date;
import java.util.Objects;

public class User extends BaseEntity {
    private Integer id;//id
    private String userName;//昵称 NotNUll
    private String userCode;//账号 NotNUll
    private String password;//密码 NotNUll
    private String name;//姓名
    private Integer sex;//性别 NotNUll
    private Integer age;//年龄 NotNUll
    private Integer fitnessDay;//健身时长(天)
    private String region;//地区
    private Date birthDate;//出生日期 NotNUll
    private Integer stature;//身高(cm)
    private Integer weight;//体重(g)
    private String phoneNumber;//手机号 NotNUll
    private String email;//邮箱
    private String motto;//个性签名
    private Integer status;//是否在线 0-离线 1-在线
    private String tonken;//登陆凭证
    private String userInfo;//用户介绍
    private Integer valid;//数据是否有效 0-无效 1-有效

    public User toResponseUser(){
        this.setTonken(null);
        this.setPassword(null);
        this.setModifyUser(null);
        this.setModifyDate(null);
        this.setAddUser(null);
        this.setAddDate(null);
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userCode='" + userCode + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", fitnessDay=" + fitnessDay +
                ", region='" + region + '\'' +
                ", birthDate=" + birthDate +
                ", stature=" + stature +
                ", weight=" + weight +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", motto='" + motto + '\'' +
                ", status=" + status +
                ", tonken='" + tonken + '\'' +
                ", userInfo='" + userInfo + '\'' +
                ", valid=" + valid +
                ", addUser=" + addUser +
                ", modifyUser=" + modifyUser +
                ", addDate=" + addDate +
                ", modifyDate=" + modifyDate +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(userName, user.userName) &&
                Objects.equals(userCode, user.userCode) &&
                Objects.equals(password, user.password) &&
                Objects.equals(name, user.name) &&
                Objects.equals(sex, user.sex) &&
                Objects.equals(age, user.age) &&
                Objects.equals(region, user.region) &&
                Objects.equals(birthDate, user.birthDate) &&
                Objects.equals(stature, user.stature) &&
                Objects.equals(weight, user.weight) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(email, user.email) &&
                Objects.equals(motto, user.motto) &&
                Objects.equals(status, user.status) &&
                Objects.equals(tonken, user.tonken) &&
                Objects.equals(userInfo, user.userInfo) &&
                Objects.equals(valid, user.valid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, userCode, password, name, sex, age, region, birthDate, stature, weight, phoneNumber, email, motto, status, tonken, userInfo, valid);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getStature() {
        return stature;
    }

    public void setStature(Integer stature) {
        this.stature = stature;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTonken() {
        return tonken;
    }

    public void setTonken(String tonken) {
        this.tonken = tonken;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public Integer getFitnessDay() {
        return fitnessDay;
    }

    public void setFitnessDay(Integer fitnessDay) {
        this.fitnessDay = fitnessDay;
    }
}
