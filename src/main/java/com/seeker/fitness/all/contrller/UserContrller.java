package com.seeker.fitness.all.contrller;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.entity.User;
import com.seeker.fitness.all.service.UserService;
import com.seeker.fitness.all.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("users")
public class UserContrller {
    private Logger log= LoggerFactory.getLogger(UserContrller.class);
    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     * @param user
     * @return
     */
    @RequestMapping("enrollUser")
    public ResponseResult enrollUserApi(@RequestBody User user){
        String interfaceName="用户注册接口";
        log.info("<<--------------"+interfaceName+"调用开始-------------->>");
        ResponseResult responseResult = userService.enrollUser(user);
        log.info("<<--------------"+interfaceName+"调用结束-------------->>");
        return responseResult;
    }

    /**
     * 用户登陆接口
     * @param loginObj
     * @param response
     * @return
     */
    @RequestMapping("login")
    public ResponseResult loginApi(@RequestBody JSONObject loginObj, HttpServletResponse response, HttpServletRequest request){
        String interfaceName="用户登陆接口";
        log.info("<<--------------"+interfaceName+"调用开始-------------->>");
        ResponseResult responseResult = userService.userLogin(loginObj,response,request);
        log.info("<<--------------"+interfaceName+"调用结束-------------->>");
        return responseResult;
    }

    /**
     * 用户密码修改
     * @param updateObj
     * @return
     */
    @RequestMapping("updatePassword")
    public ResponseResult updatePassword(@RequestBody JSONObject updateObj){
        String interfaceName="用户密码修改";
        log.info("<<--------------"+interfaceName+"调用开始-------------->>");
        ResponseResult responseResult = userService.passwordModify(updateObj);
        log.info("<<--------------"+interfaceName+"调用结束-------------->>");
        return responseResult;
    }

    /**
     * 用户资料修改
     * @param request
     * @param user
     * @return
     */
    @RequestMapping("updateUserData")
    public ResponseResult updateUserData(HttpServletRequest request,@RequestBody User user){
        String interfaceName="用户资料修改";
        log.info("<<--------------"+interfaceName+"调用开始-------------->>");
        ResponseResult responseResult = userService.userDataModify(request,user);
        log.info("<<--------------"+interfaceName+"调用结束-------------->>");
        return responseResult;
    }


    }
