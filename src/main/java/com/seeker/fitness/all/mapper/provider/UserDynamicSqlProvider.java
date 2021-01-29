package com.seeker.fitness.all.mapper.provider;

import com.seeker.fitness.all.entity.User;
import org.apache.ibatis.jdbc.SQL;

public class UserDynamicSqlProvider {
    public String updateUser(User user){
        return new SQL(){
            {
                UPDATE("user_list");
                if(user.getUserName()!=null){
                    SET("user_name=#{userName}");
                }
                if(user.getPassword()!=null){
                    SET("password=#{password}");
                }
                if(user.getName()!=null){
                    SET("name=#{name}");
                }
                if(user.getSex()!=null){
                    SET("sex=#{sex}");
                }
                if(user.getAge()!=null){
                    SET("age=#{age}");
                }
                if(user.getFitnessDay()!=null){
                    SET("fitness_day=#{fitnessDay}");
                }
                if(user.getRegion()!=null){
                    SET("region=#{region}");
                }
                if(user.getBirthDate()!=null){
                    SET("birth_date=#{birthDate}");
                }
                if(user.getStature()!=null){
                    SET("stature=#{stature}");
                }
                if(user.getWeight()!=null){
                    SET("weight=#{weight}");
                }
                if(user.getPhoneNumber()!=null){
                    SET("phone_number=#{phoneNumber}");
                }
                if(user.getEmail()!=null){
                    SET("email=#{email}");
                }
                if(user.getMotto()!=null){
                    SET("motto=#{motto}");
                }
                if(user.getStatus()!=null){
                    SET("status=#{status}");
                }
                if(user.getToken()!=null){
                    SET("token=#{token}");
                }
                if(user.getSalt()!=null){
                    SET("salt=#{salt}");
                }
                if(user.getUserInfo()!=null){
                    SET("user_info=#{userInfo}");
                }
                if(user.getValid()!=null){
                    SET("valid=#{valid}");
                }
                if(user.getModifyUser()!=null){
                    SET("modify_user=#{modifyUser}");
                }
                if(user.getModifyDate()!=null){
                    SET("modify_date=#{modifyDate}");
                }
                WHERE("user_code=#{userCode}");
            }
        }.toString();
    }
}
