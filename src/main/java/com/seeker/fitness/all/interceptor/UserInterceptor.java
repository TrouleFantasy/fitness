package com.seeker.fitness.all.interceptor;

import com.seeker.fitness.all.util.PracticalUtil;
import com.seeker.fitness.all.util.ResponseResult;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取用户携带的token
        String token=request.getHeader("token");
        //判断其token是否正确且有效
        if(!StringUtils.isEmpty(token)&&PracticalUtil.isTokenValid(token)){
            //有效则继续其请求并且为当前token续期
            PracticalUtil.tokenRenewal(token);
            return true;
        }
        //否则进行阻拦操作
        // 创建返回对象
        ResponseResult responseResult=new ResponseResult();
        responseResult.setStatus(-1);
        responseResult.setMessage("用户未登录！");
        //设置返回信息
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter printWriter=response.getWriter();
        printWriter.write(responseResult.toJSONString());
        //终止其请求
        return false;
    }

}
