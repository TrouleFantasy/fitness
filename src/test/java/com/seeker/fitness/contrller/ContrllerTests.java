package com.seeker.fitness.contrller;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.entity.User;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        loginObj.put("password","qwer12345");

        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity=new HttpEntity<>(JSONObject.toJSONString(loginObj),headers);
        ResponseEntity result=restTemplate.postForEntity("http://127.0.0.1:8081/users/login",httpEntity,String.class);
        System.out.println(JSONObject.toJSONString(result));
    }
    @Test
    public void updatePasswordTest(){
        JSONObject updateObj=new JSONObject();
        updateObj.put("userCode","niceMy2015");
        updateObj.put("oldPassword","qwer1234");
        updateObj.put("newPassword","qwer12345");
        updateObj.put("reNewPassword","qwer12345");

        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token","");
        HttpEntity<String> httpEntity=new HttpEntity<>(JSONObject.toJSONString(updateObj),headers);
        String result=restTemplate.postForObject("http://127.0.0.1:8081/users/updatePassword",httpEntity,String.class);
        System.out.println(result);
    }

    @Test
    public void queryFoodsByNameTest(){
        JSONObject foodFindObj=new JSONObject();
        foodFindObj.put("name","肉");

        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOiIzMjIwNzE3MjA2ODUwIiwiaWF0IjoiMTYxMDM1ODYwMzQyNSIsImp0aSI6IjJiMDVmZTZlZDMiLCJ1c2VyQ29kZSI6Im5pY2VNeTIwMTUifQ==.9a31aaa6288e8c9586eeed4d10b94615cd6942708df5177fe9e2f85f908fc92b");
        HttpEntity<String> httpEntity=new HttpEntity<>(JSONObject.toJSONString(foodFindObj),headers);
        String result=restTemplate.postForObject("http://127.0.0.1:8080/Fitness/foods/queryFoodsByName",httpEntity,String.class);
        System.out.println(result);
    }
}
