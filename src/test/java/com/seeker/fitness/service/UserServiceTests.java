package com.seeker.fitness.service;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.entity.User;
import com.seeker.fitness.all.service.UserService;
import com.seeker.fitness.all.util.ResponseResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @Test
    public void enrollUserTest(){
        try {
            User user=new User();
            user.setUserName("弱鸡");//昵称
            user.setUserCode("niceMy2015");//账号
            user.setPassword("qwer1234");//密码
            user.setSex(1);//性别
            Date date=new SimpleDateFormat("yyyy-MM-dd").parse("1999-07-13");
            user.setBirthDate(date);//出生日期
            user.setPhoneNumber("15233571928");//手机号
            user.setRegion("北京");//地区
            ResponseResult responseResult=userService.enrollUser(user);
            System.out.println(JSONObject.toJSONString(responseResult));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
