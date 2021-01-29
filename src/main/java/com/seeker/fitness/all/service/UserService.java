package com.seeker.fitness.all.service;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.entity.User;
import com.seeker.fitness.all.util.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 本接口定义了用户相关的业务
 */
public interface UserService {
    /**
     * 用户注册
     * @param user
     * @return
     */
    ResponseResult enrollUser(User user);

    /**
     * 用户登陆
     * @param loginObj
     * @return
     */
    ResponseResult userLogin(JSONObject loginObj, HttpServletResponse response, HttpServletRequest request);

    /**
     * 用户通过旧密码 修改密码
     * @param updateObj
     * @return
     */
    ResponseResult passwordModify(JSONObject updateObj);

    /**
     * 用户资料修改
     * @param user
     * @return
     */
    ResponseResult userDataModify(HttpServletRequest request,User user);
}
