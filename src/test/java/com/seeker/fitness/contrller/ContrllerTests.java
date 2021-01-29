package com.seeker.fitness.contrller;

import com.alibaba.fastjson.JSONArray;
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
        JSONObject enrollUser=new JSONObject();
        try{
        enrollUser.put("userName","fitness管理");//昵称
        enrollUser.put("userCode","fitnessAdmin");//账号
        enrollUser.put("password","fitnessAdmin");//密码
        enrollUser.put("sex",1);//性别
        Date date=new SimpleDateFormat("yyyy-MM-dd").parse("1999-07-13");
        enrollUser.put("birthDate",date);//出生日期
        enrollUser.put("phoneNumber","15233571929");//手机号
        enrollUser.put("region","北京");//地区
        } catch (ParseException e) {
            e.printStackTrace();
        }
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity=new HttpEntity<>(JSONObject.toJSONString(enrollUser),headers);
        String result=restTemplate.postForObject("http://127.0.0.1:8081/users/enrollUser",httpEntity,String.class);
        System.out.println(result);
    }
    @Test
    public void loginTest(){
        JSONObject loginObj=new JSONObject();
        loginObj.put("userCode","fitnessAdmin");
        loginObj.put("password","fitnessAdmin");

        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        headers.set("User-Agent","LocalHostTest");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity=new HttpEntity<>(JSONObject.toJSONString(loginObj),headers);
        ResponseEntity result=restTemplate.postForEntity("http://127.0.0.1:8081/users/login",httpEntity,String.class);
        System.out.println(JSONObject.toJSONString(result));
    }
    @Test
    public void updatePasswordTest(){
        JSONObject updateObj=new JSONObject();
        updateObj.put("userCode","niceMy2015");
        updateObj.put("oldPassword","qwer12345");
        updateObj.put("newPassword","qwer1234");
        updateObj.put("reNewPassword","qwer1234");

        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token","");
        HttpEntity<String> httpEntity=new HttpEntity<>(JSONObject.toJSONString(updateObj),headers);
        String result=restTemplate.postForObject("http://127.0.0.1:8081/users/updatePassword",httpEntity,String.class);
        System.out.println(result);
    }
    @Test
    public void updateUserDataTest(){
        User user=new User();
        user.setUserCode("niceMy2015");
        user.setUserName("大神");
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token","");
        HttpEntity<String> httpEntity=new HttpEntity<>(JSONObject.toJSONString(user),headers);
        String result=restTemplate.postForObject("http://127.0.0.1:8081/users/updateUserData",httpEntity,String.class);
        System.out.println(result);
    }

    @Test
    public void queryFoodsByNameTest(){
        JSONObject foodFindObj=new JSONObject();
        foodFindObj.put("name","肉");

        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token","");
        HttpEntity<String> httpEntity=new HttpEntity<>(JSONObject.toJSONString(foodFindObj),headers);
        String result=restTemplate.postForObject("http://127.0.0.1:8081/foods/queryFoodsByName",httpEntity,String.class);
        System.out.println(result);
    }
    @Test
    public void getTableInfoTest(){
        JSONObject tableInfoObj=new JSONObject();
        tableInfoObj.put("userCode","fitnessAdmin");
        tableInfoObj.put("keepsake","王中王的火腿肠");
        tableInfoObj.put("tableName","user_list");

        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token","eyJhbGciOiJTSEEyNTZIZXgiLCJ0eXAiOiJKV1QifQ==.eyJleHAiOiIzMjIzMjk4MDAzNjc2IiwiaWF0IjoiMTYxMTY0OTAwMTgzOSIsImp0aSI6ImUyN2QwNGNjYzkiLCJ1c2VyQ29kZSI6ImZpdG5lc3NBZG1pbiJ9.d343b9fce6238ebf874eab0c1889ddec3a3115d53f3e3daa87d866c82ed7bf7a");
        headers.set("User-Agent","LocalHostTest");
        HttpEntity<String> httpEntity=new HttpEntity<>(JSONObject.toJSONString(tableInfoObj),headers);
        String result=restTemplate.postForObject("http://127.0.0.1:8081/admin/getTableInfo",httpEntity,String.class);
        System.out.println(result);
        JSONObject json=JSONObject.parseObject(result);
        JSONArray arr=json.getJSONArray("data");
        for (Object o : arr) {
            System.out.println(JSONObject.toJSONString(o));
        }
    }
}
