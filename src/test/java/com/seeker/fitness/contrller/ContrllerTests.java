package com.seeker.fitness.contrller;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.entity.User;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
public class ContrllerTests {

    @Test
    public void aaa(){
        User user=new User();
            user.setUserName("弱鸡2");//昵称
            user.setUserCode("niceMy201503");//账号
            user.setPassword("qwer1234");//密码
            user.setSex(1);//性别
        Date date= null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("1999-07-13");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setBirthDate(date);//出生日期
            user.setPhoneNumber("15233571929");//手机号
            user.setRegion("北京");//地区
     System.out.println(JSONObject.toJSONString(user));
    }
    @Test
    public void enrollUserTest(){
        User user=new User();
        try{
        user.setUserName("弱鸡2");//昵称
        user.setUserCode("niceMy201503");//账号
        user.setPassword("qwer1234");//密码
        user.setSex(1);//性别
        Date date=new SimpleDateFormat("yyyy-MM-dd").parse("1999-07-13");
        user.setBirthDate(date);//出生日期
        user.setPhoneNumber("15233571929");//手机号
        user.setRegion("北京");//地区
        } catch (ParseException e) {
            e.printStackTrace();
        }
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity=new HttpEntity<>(JSONObject.toJSONString(user),headers);
        String result=restTemplate.postForObject("http://127.0.0.1:8081/users/enrollUser",httpEntity,String.class);
        System.out.println(result);
    }
    @Test
    public void loginTest(){
        JSONObject loginObj=new JSONObject();
        loginObj.put("userCode","niceMy2015");
        loginObj.put("password","qwer1234");

        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity=new HttpEntity<>(JSONObject.toJSONString(loginObj),headers);
        String result=restTemplate.postForObject("http://127.0.0.1:8081/users/login",httpEntity,String.class);
        System.out.println(result);
    }

    @Test
    public void queryFoodsByNameTest(){
        JSONObject foodFindObj=new JSONObject();
        foodFindObj.put("name","肉");

        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity=new HttpEntity<>(JSONObject.toJSONString(foodFindObj),headers);
        String result=restTemplate.postForObject("http://127.0.0.1:8081/foods/queryFoodsByName",httpEntity,String.class);
        System.out.println(result);
    }
}
