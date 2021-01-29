package com.seeker.fitness.all.contrller;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.entity.User;
import com.seeker.fitness.all.service.UserService;
import com.seeker.fitness.all.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("users")
public class UserContrller {
    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     * @param user
     * @return
     */
    @RequestMapping("enrollUser")
    public ResponseResult enrollUserApi(@RequestBody User user){
        return userService.enrollUser(user);
    }

    /**
     * 用户登陆接口
     * @param loginObj
     * @param response
     * @return
     */
    @RequestMapping("login")
    public ResponseResult loginApi(@RequestBody JSONObject loginObj, HttpServletResponse response, HttpServletRequest request){
        return userService.userLogin(loginObj,response,request);
    }

    /**
     * 用户密码修改
     * @param updateObj
     * @return
     */
    @RequestMapping("updatePassword")
    public ResponseResult updatePassword(@RequestBody JSONObject updateObj){
        return userService.passwordModify(updateObj);
    }

    /**
     * 用户资料修改
     * @param request
     * @param user
     * @return
     */
    @RequestMapping("updateUserData")
    public ResponseResult updateUserData(HttpServletRequest request,@RequestBody User user){
        return userService.userDataModify(request,user);
    }


    }
