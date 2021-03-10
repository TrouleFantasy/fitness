package com.seeker.fitness.all.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.config.ConfigParamMapping;
import com.seeker.fitness.all.entity.User;
import com.seeker.fitness.all.ex.DataBasesException;
import com.seeker.fitness.all.ex.ServiceException;
import com.seeker.fitness.all.mapper.fitnessmapper.UserMapper;
import com.seeker.fitness.all.service.UserService;
import com.seeker.fitness.all.util.PracticalUtil;
import com.seeker.fitness.all.util.ResponseResult;
import com.seeker.fitness.all.util.Token;
import com.seeker.fitness.all.util.redis.RedisFactory;
import com.seeker.fitness.all.util.redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private Logger log= LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserMapper userMapper;
    /**
     *用户注册接口
     * @param user
     * @return
     */
    public ResponseResult enrollUser(User user) {
        String interfaceName="用户注册接口";
        log.info(interfaceName+"入参："+JSONObject.toJSONString(user));
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
        try {
            //开始数据操作
            //制造盐值
            String salt = PracticalUtil.createSecureSalt();
            user.setSalt(salt);
            //密码加密
            String Md5Password = PracticalUtil.createSecurePassword(user.getPassword(), salt);
            user.setPassword(Md5Password);
            //计算年龄
            Date birthDate = user.getBirthDate();
            Integer age = PracticalUtil.getAgeByBirthDate(birthDate);
            user.setAge(age);
            //开始写入数据库
            Integer resultInt = addUser(user);
            if (resultInt != 1) {
                throw new DataBasesException("注册失败！请联系管理员！");
            }
            ResponseResult responseResult=ResponseResult.successResponse();
            log.info(interfaceName+"反参："+JSONObject.toJSONString(responseResult));
            return responseResult;
        }catch (Exception e){
            e.printStackTrace();
            ResponseResult responseResult=ResponseResult.errorResponse("注册失败！请联系管理员！");
            log.info(interfaceName+"反参："+JSONObject.toJSONString(responseResult));
            return responseResult;
        }
    }

    /**
     * 用户登录接口
     * @param loginObj
     * @return
     */
    public ResponseResult userLogin(JSONObject loginObj, HttpServletResponse response, HttpServletRequest request) {
        String interfaceName="用户登录接口";
        log.info(interfaceName+"入参："+JSONObject.toJSONString(loginObj));
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
            //根据账号查找对应用户信息
            User resultUser=userMapper.getUserByUserCode(userCode);
            if(resultUser==null||resultUser.getValid()!=1){
                return ResponseResult.errorResponse("无效用户！");
            }
            //判断密码正确性
            //获取数据库中储存的用户密码与盐值
            String resultPassword=resultUser.getPassword();
            String salt=resultUser.getSalt();
            //将入参中的密码加密后与数据库中储存的密码对比 如果不一致则返回错误
            if(!PracticalUtil.comparisonPassword(resultPassword,password,salt)){
                return ResponseResult.errorResponse("密码不正确！");
            }

            //第1种情况-已经登陆过
            //由于token的发放需要细化到访问者的 访问工具 每台设备相同的访问工具只能拥有一个token 所以需要校验
            //获取用户设备的 ip地址   获取用户的 客户端信息
            String userAgentInfo=PracticalUtil.getIpAddress(request)+ConfigParamMapping.getTokenSpaceMark()+request.getHeader("User-Agent");
            //判断登陆记录表中是否有此用户
            boolean sismember=jedis.sismember(ConfigParamMapping.getRedisLoginTableName(),userCode);
            //如果为true 则说明这个用户已经登陆 则去查该用户对应的token信息表
            if(sismember){
                //以下代码均为了确定此次请求登陆的用户是否是在同一设备上的同一客户端登陆 如不是则可以继续登陆验证
                Map<String, String> userTokenInfoMap=jedis.hgetAll(userCode);
                Set<Map.Entry<String,String>> entrySet=userTokenInfoMap.entrySet();
                for(Map.Entry<String,String> entry:entrySet){
                    String key=entry.getKey();
                    String userTokenInfo=entry.getValue();
                    //取出token信息中 用户信息段
                    String resultUserAgentInfo=PracticalUtil.headGetStringByRegex(".*?"+ConfigParamMapping.getTokenSpaceMark(),userTokenInfo,true);
                    if(userAgentInfo.equals(resultUserAgentInfo)){
                        //此做法不适用于 用户关闭浏览器后 丢失token的情况 如果出现这种情况 用户只能等到token到期后才能够再次登陆
                        //return ResponseResult.errorResponse("用户已登陆,请勿重复操作！");

                        //所以可以将之前的token返回给用户 并刷新有效时间
                        //将token放置在响应头中
                        response.setHeader("token",key);
                        //刷新有效时间
                        RedisUtil.tokenRenewal(key,jedis);
                        //返回对应用户信息
                        return  ResponseResult.successResponse(resultUser.toResponseUser());
                    }
                }
            }


            //第2种情况-未登陆过
            //如果密码正确且没有用同一设备同一客户端登陆过则准许登录 返回用户信息并发放token

            //并且这种情况下有可能会超出最大登陆数的限制 如果超出则下线最早登陆的那一个
            //判断该用户在此次登陆后是否会超出最大登陆数
            if(jedis.hlen(userCode)>=ConfigParamMapping.getTokenTotal()){
                //首先清理该用户不合法的token 包括超出数量的和已到期的
                RedisUtil.violateToken(userCode,jedis);
                //由于以上方法只会将token数控制在最大数量 但是我们现在需要最大数量-1个已有token 否则新发的token又将会超出最大数量 所以需要继续删除一个
                //获取最先到期的那一个
                String recentToken=RedisUtil.getRecentToken(userCode,jedis);
                //删除最先到期的那一个
                RedisUtil.deleteToken(userCode,recentToken,jedis);
            }

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
            jedis.sadd(ConfigParamMapping.getRedisLoginTableName(),userCode);
            //设置一个hash表 表名为对应的userCode token为键 过期时间为值
            String tokenInfo=PracticalUtil.getTimeStamp(ConfigParamMapping.getTokenTimeOut())+ConfigParamMapping.getTokenSpaceMark()+userAgentInfo;
            jedis.hset(userCode,token,tokenInfo);

            ResponseResult responseResult=ResponseResult.successResponse(resultUser.toResponseUser());
            log.info(interfaceName+"反参："+JSONObject.toJSONString(responseResult));
            return responseResult;

        }catch (Exception e){
            e.printStackTrace();
            ResponseResult responseResult=ResponseResult.errorResponse("解析报文时出现异常！");
            log.info(interfaceName+"反参："+JSONObject.toJSONString(responseResult));
            return responseResult;
        }finally {
            //释放连接
            jedis.close();
        }

    }

    /**
     * 用户通过旧密码修改密码
     * @param updateObj
     * @return
     */
    public ResponseResult passwordModify(JSONObject updateObj) {
        String interfaceName="用户通过旧密码修改密码";
        log.info(interfaceName+"入参："+JSONObject.toJSONString(updateObj));
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
            String secureNewPassword=PracticalUtil.createSecurePassword(newPassword,resultSalt);
            newPassUser.setPassword(secureNewPassword);
            Integer resultInt=updateUser(newPassUser);
            if(resultInt!=1){
                throw new DataBasesException("密码修改失败！请联系管理员！");
            }
            //密码修改后 当前该用户所有的token都应当注销 删除该用户所有的token
            RedisUtil.cleanAllToken(userCode);

            ResponseResult responseResult=ResponseResult.successResponse("密码修改成功！");
            log.info(interfaceName+"反参："+JSONObject.toJSONString(responseResult));
            return responseResult;
        }catch (ServiceException e){
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            return ResponseResult.errorResponse("密码修改失败！");
        }
    }

    /**
     * 用户资料修改
     * @param user
     * @return
     */
    public ResponseResult userDataModify(HttpServletRequest request,User user) {
        String interfaceName="用户资料修改";
        log.info(interfaceName+"入参："+JSONObject.toJSONString(user));
        if(user==null||StringUtils.isEmpty(user.getUserCode())||StringUtils.isEmpty(user.getUserName())){
            return ResponseResult.errorResponse("参数错误！修改失败！");
        }
        //获取用户携带的token
        String token=request.getHeader("token");
        log.info(interfaceName+"入参中的token："+token);
        Token tokenObj=Token.parseTokenObj(token);

        ResponseResult responseResult;
        if(!user.getUserCode().equals(tokenObj.getUserCode())){
            responseResult=ResponseResult.errorResponse("修改非法!");
            log.info(interfaceName+"反参："+JSONObject.toJSONString(responseResult));
            return responseResult;
        }
        String newUserName=user.getUserName();
        if(userMapper.countUserByUserName(newUserName)!=0){
            responseResult=ResponseResult.errorResponse(999,"昵称已存在！请重新输入!");
            log.info(interfaceName+"反参："+JSONObject.toJSONString(responseResult));
            return responseResult;
        }
        Integer resultInt=updateUser(user);
        if(resultInt!=1){
            responseResult=ResponseResult.errorResponse(999,"资料修改失败！请联系管理员！");
            log.info(interfaceName+"反参："+JSONObject.toJSONString(responseResult));
            return responseResult;
        }

        responseResult=ResponseResult.successResponse();
        log.info(interfaceName+"反参："+JSONObject.toJSONString(responseResult));
        return responseResult;
    }
    /**
     * 封装mapper方法简化操作
     * @param user
     * @return
     */
    private Integer updateUser(User user){
        user.setModifyDate(new Date());
        user.setModifyUser(user.getUserCode());
        return userMapper.updateUser(user);
    }

    /**
     * 封装mapper方法简化操作
     * @param user
     * @return
     */
    private Integer addUser(User user){
        //设置日志项以及有效标记
        user.setAddDate(new Date());
        user.setAddUser("0");
        user.setModifyDate(new Date());
        user.setModifyUser("0");
        user.setValid(1);
        return  userMapper.addUser(user);
    }
}
