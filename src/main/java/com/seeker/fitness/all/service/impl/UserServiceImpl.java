package com.seeker.fitness.all.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.entity.User;
import com.seeker.fitness.all.mapper.UserMapper;
import com.seeker.fitness.all.service.UserService;
import com.seeker.fitness.all.util.PracticalUtil;
import com.seeker.fitness.all.util.ResponseResult;
import com.seeker.fitness.all.util.redis.RedisFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Value("${user.tokenTimeOut}")
    private Long tokenTimeOut;
    /**
     *用户注册接口
     * @param user
     * @return
     */
    public ResponseResult enrollUser(User user) {
        //开始判空
        if(StringUtils.isEmpty(user.getUserCode())){
            return ResponseResult.errorResponse("userCoce字段不能为空！");
        }
        if(StringUtils.isEmpty(user.getUserName())){
            return ResponseResult.errorResponse("userName字段不能为空！");
        }
        User resultUserByUserCode=userMapper.getUserByUserCode(user.getUserCode());
        if(resultUserByUserCode!=null){
            return ResponseResult.errorResponse("已存在的用户！");
        }
        User resultUserByName=userMapper.getUserByUserName(user.getUserName());
        if(resultUserByName!=null){
            return ResponseResult.errorResponse("昵称已存在！");
        }
        if(StringUtils.isEmpty(user.getPassword())){
            return ResponseResult.errorResponse("password字段不能为空！");
        }
        if(StringUtils.isEmpty(user.getSex())){
            return ResponseResult.errorResponse("sex字段不能为空！");
        }
        if(StringUtils.isEmpty(user.getBirthDate())){
            return ResponseResult.errorResponse("birthDate字段不能为空！");
        }
        if(StringUtils.isEmpty(user.getPhoneNumber())){
            return ResponseResult.errorResponse("phoneNumber字段不能为空！");
        }
        //开始数据操作
        //密码加密
        String password=user.getPassword();
        String Md5Password= DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(Md5Password);
        //计算年龄
        Date birthDate=user.getBirthDate();
        Integer age= PracticalUtil.getAgeByBirthDate(birthDate);
        user.setAge(age);
        //设置四项日志
        user.setAddDate(new Date());
        user.setAddUser(0);
        user.setModifyDate(new Date());
        user.setModifyUser(0);
        //开始写入数据库
        Integer resultInt=userMapper.addUser(user);
        if(resultInt!=1){
            return ResponseResult.errorResponse("写入信息时发生异常！请联系管理员！");
        }
        return ResponseResult.successResponse();
    }

    /**
     * 用户登录接口
     * @param loginObj
     * @return
     */
    public ResponseResult userLogin(JSONObject loginObj, HttpServletResponse response) {
        String userCode=loginObj.getString("userCode");
        String password=loginObj.getString("password");
        if(StringUtils.isEmpty(userCode)){
            return ResponseResult.errorResponse("userCode不得为空！");
        }
        if(StringUtils.isEmpty(password)){
            return ResponseResult.errorResponse("password不得为空！");
        }

        //判断密码正确性
        //根据账号查找对应用户信息
        User resultUser=userMapper.getUserByUserCode(userCode);
        //获取数据库中储存的用户密码
        String resultPassword=resultUser.getPassword();
        //将入参中的密码加密后与数据库中储存的密码对比
        String md5Password=DigestUtils.md5DigestAsHex(password.getBytes());
        //如果不一致则返回错误
        if(!resultPassword.equals(md5Password)){
            return ResponseResult.errorResponse("密码不正确！");
        }

        //如果一致则准许登录 返回用户信息并发放token
        //生成token
        String token=PracticalUtil.getToken(userCode);
        //将token放置在响应头中
        response.setHeader("token",token);
        System.out.println(token);
        //写入redis
        //获取一个redis连接
        Jedis jedis=RedisFactory.getJedis();
        //设置一个键值对 并设置超时时间 此时间就是token的过期时间
        jedis.set(token,userCode);
        jedis.expireAt(token,PracticalUtil.getTimeStamp(tokenTimeOut));
        //释放连接
        jedis.close();
        return ResponseResult.successResponse(resultUser.toResponseUser());
    }
}
