package com.seeker.fitness.all.mapper;

import com.seeker.fitness.all.entity.User;
import com.seeker.fitness.all.mapper.provider.UserDynamicSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {
    /**
     * 新增一个用户
     * @param user
     * @return
     */
    @Insert("INSERT INTO user_list(user_name,user_code,password,name,sex,age,fitness_day,region,birth_date,stature,weight,phone_number,email,motto,status,tonken,user_info,valid,add_user,modify_user,add_date,modify_date) VALUES(#{userName},#{userCode},#{password},#{name},#{sex},#{age},#{fitnessDay},#{region},#{birthDate},#{stature},#{weight},#{phoneNumber},#{email},#{motto},#{status},#{tonken},#{userInfo},#{valid},#{addUser},#{modifyUser},#{addDate},#{modifyDate})")
    Integer addUser(User user);

    /**
     * 查询所有用户
     * @return
     */
    @Select("SELECT * FROM user_list")
    @Results(id = "userMap", value = {
            @Result(property = "userName",column = "user_name"),
            @Result(property = "userCode",column = "user_code"),
            @Result(property = "birthDate",column = "birth_date"),
            @Result(property = "fitnessDay",column = "fitness_day"),
            @Result(property = "phoneNumber",column = "phone_number"),
            @Result(property = "userInfo",column = "user_info"),
            @Result(property = "addUser",column = "add_user"),
            @Result(property = "modifyUser",column = "modify_user"),
            @Result(property = "addDate",column = "add_date"),
            @Result(property = "modifyDate",column = "modify_date")
    })
    List<User> getUserAll();

    /**
     * 根据账号查找指定用户
     * @param userCode
     * @return
     */
    @Select("SELECT * FROM user_list WHERE user_code=#{userCode}")
    @ResultMap("userMap")
    User getUserByUserCode(String userCode);

    /**
     * 根据昵称查找指定用户
     * @param userName
     * @return
     */
    @Select("SELECT * FROM user_list WHERE user_name=#{userName}")
    @ResultMap("userMap")
    User getUserByUserName(String userName);

//    @Update("<script> UPDATE user_list <set>"+
////            "<if test=\"userName != null\">user_name=#{userName}</if>"+
////            "<if test=\"password != null\">, password=#{password}</if>"+
////            "<if test=\"phoneNumber != null\">, phone_number=#{phoneNumber}</if>"+
////            "<if test=\"email != null\">, email=#{email}</if>"+
////            "<if test=\"motto != null\">, motto=#{motto}</if>"+
////            "<if test=\"status != null\">, status=#{status}</if>"+
////            "<if test=\"tonken != null\">, tonken=#{tonken}</if>"+
////            "<if test=\"userInfo != null\">, user_info=#{userInfo}</if>"+
////            "<if test=\"valid != null\">, valid=#{valid}</if>"+
////            "<if test=\"modifyUser != null\">, modify_user=#{modifyUser}</if>"+
////            "<if test=\"modifyDate != null\">, modify_date=#{modifyDate}</if></set>"+
////            " WHERE id = #{id}</script>")
    @UpdateProvider(type = UserDynamicSqlProvider.class,method = "updateUser")
    Integer updateUser(User user);
}
