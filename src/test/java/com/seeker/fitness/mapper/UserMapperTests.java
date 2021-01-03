package com.seeker.fitness.mapper;

import com.seeker.fitness.all.entity.User;
import com.seeker.fitness.all.mapper.UserMapper;
import com.seeker.fitness.all.util.PracticalUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class UserMapperTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void addUserTest(){
        User user=new User();
        user.setUserName("测试");
        user.setUserCode("test202012281720");
        user.setPassword("123456789");
        user.setSex(1);
        Date birDate=null;
        try {
            birDate=new SimpleDateFormat("yyyy-MM-dd").parse("1999-07-13");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setBirthDate(birDate);
        user.setAge(PracticalUtil.getAgeByBirthDate(birDate));
        user.setPhoneNumber("15233571928");
        user.setAddUser(0);
        user.setAddDate(new Date());
        user.setModifyUser(0);
        user.setModifyDate(new Date());
        Integer state=userMapper.addUser(user);
        System.out.println("state:"+state);
    }

    @Test
    public void getUserAllTest(){
        List<User> list=userMapper.getUserAll();
        for(User user:list){
            System.out.println(user);
        }

    }

    @Test
    public void updateUserTest(){
        List<User> list=userMapper.getUserAll();
        for (User user:list){
            if(user.getId()==2){
                User user1=new User();
                user1.setId(user.getId());
//                user1.setUserName("测试改后");
                user1.setMotto("测试测试");
                user1.setModifyUser(user.getId());
                user1.setModifyDate(new Date());
                Integer state=userMapper.updateUser(user1);
                System.out.println("state:"+state);
            }
        }
    }

}
