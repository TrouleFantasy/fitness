package com.seeker.fitness.all.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.config.ConfigParamMapping;
import com.seeker.fitness.all.entity.User;
import com.seeker.fitness.all.ex.DataBasesException;
import com.seeker.fitness.all.ex.ServiceException;
import com.seeker.fitness.all.mapper.UserMapper;
import com.seeker.fitness.all.service.UserService;
import com.seeker.fitness.all.util.PracticalUtil;
import com.seeker.fitness.all.util.ResponseResult;
import com.seeker.fitness.all.util.Token;
import com.seeker.fitness.all.util.redis.RedisFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
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
        //制造盐值
        String salt=PracticalUtil.createSecureSalt();
        user.setSalt(salt);
        //密码加密
        String Md5Password=PracticalUtil.createSecurePassword(user.getPassword(),salt);
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
            throw new DataBasesException("注册失败！请联系管理员！");
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
        //获取一个redis连接
        Jedis jedis=RedisFactory.getJedis();
        try {
            //判断密码正确性
            //根据账号查找对应用户信息
            User resultUser=userMapper.getUserByUserCode(userCode);
            if(resultUser==null||resultUser.getValid()!=1){
                return ResponseResult.errorResponse("无效用户！");
            }
            //获取数据库中储存的用户密码与盐值
            String resultPassword=resultUser.getPassword();
            String salt=resultUser.getSalt();
            //将入参中的密码加密后与数据库中储存的密码对比 如果不一致则返回错误
            if(!PracticalUtil.comparisonPassword(resultPassword,password,salt)){
                return ResponseResult.errorResponse("密码不正确！");
            }

            //如果一致则准许登录 返回用户信息并发放token
            //生成token
            Token tokenObj=new Token();
            tokenObj.setUserCode(userCode);
            tokenObj.setJti(PracticalUtil.createSecureSalt());
            tokenObj.setExp(String.valueOf(PracticalUtil.getTimeStamp(new Date().getTime())));//到期时间
            tokenObj.setIat(String.valueOf(new Date().getTime()));//签发时间
            String token= tokenObj.toTokenString();
            //将token放置在响应头中
            response.setHeader("token",token);
            //写入redis
            //向用户redis登陆集合中放入此登陆用户
            jedis.sadd("loginUserList",userCode);
            //设置一个hash表 表名为对应的userCode token为键 过期时间为值
            jedis.hset(userCode,token,String.valueOf(PracticalUtil.getTimeStamp(ConfigParamMapping.getTokenTimeOut())));
            return ResponseResult.successResponse(resultUser.toResponseUser());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseResult.errorResponse("解析报文时出现异常！");
        }finally {
            //释放连接
            jedis.close();
        }

    }

    /**
     * 用户通过旧密码 修改密码
     * @param updateObj
     * @return
     */
    public ResponseResult passwordModify(JSONObject updateObj) {
        try{
            String userCode=updateObj.getString("userCode");
            String oldPassword=updateObj.getString("oldPassword");
            String newPassword=updateObj.getString("newPassword");
            String reNewPassword=updateObj.getString("reNewPassword");
            if(StringUtils.isEmpty(userCode)){
                return ResponseResult.errorResponse("userCode字段不能为空！");
            }
            if(StringUtils.isEmpty(oldPassword)){
                return ResponseResult.errorResponse("password字段不能为空！");
            }
            if(StringUtils.isEmpty(newPassword)){
                return ResponseResult.errorResponse("newPassword字段不能为空！");
            }
            if(StringUtils.isEmpty(reNewPassword)){
                return ResponseResult.errorResponse("reNewPassword字段不能为空！");
            }
            User resultUser=userMapper.getUserByUserCode(userCode);
            String resultPassword=resultUser.getPassword();
            String resultSalt=resultUser.getSalt();
            if(!PracticalUtil.comparisonPassword(resultPassword,oldPassword,resultSalt)){
                return ResponseResult.errorResponse("旧密码输入错误！");
            }
            if(!newPassword.equals(reNewPassword)){
                return ResponseResult.errorResponse("两次新密码输入不一致！");
            }

            //开始数据操作
            User newPassUser=new User();
            newPassUser.setId(resultUser.getId());
            String secureNewPassword=PracticalUtil.createSecurePassword(newPassword,resultSalt);
            newPassUser.setPassword(secureNewPassword);
            Integer resultInt=userMapper.updateUser(newPassUser);
            if(resultInt!=1){
                throw new DataBasesException("密码修改失败！请联系管理员！");
            }
            return ResponseResult.successResponse("密码修改成功！");
        }catch (ServiceException e){
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            return ResponseResult.errorResponse("密码修改失败！");
        }
    }
}
