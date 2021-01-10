package com.seeker.fitness.all.contrller;

import com.seeker.fitness.all.ex.AddFoodException;
import com.seeker.fitness.all.ex.DataBasesException;
import com.seeker.fitness.all.ex.InputAnomalyException;
import com.seeker.fitness.all.ex.ServiceException;
import com.seeker.fitness.all.util.ResponseResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

/**
 * 错误代码
 * 4**客户端
 * 5**服务端
 * 51** 业务层相关
 * 52** 持久层相关
 */
@RestControllerAdvice
public class BaseCotrller {

    @ExceptionHandler({ServiceException.class})
    public ResponseResult<Void> serviceHandle(Throwable ex){
        ResponseResult<Void> responseResult=new ResponseResult<>(ex);
        if(ex instanceof AddFoodException){
            //5101新增食物异常
            responseResult.setStatus(5101);
        }else if(ex instanceof InputAnomalyException){
            //400 客户端入参异常
            responseResult.setStatus(400);
        }else if(ex instanceof IOException){
            //500
            responseResult.setStatus(500);
            responseResult.setMessage("获取IO流异常!");
        }else if(ex instanceof DataBasesException){
            //999 数据相关错误
            responseResult.setStatus(999);
        }
        return responseResult;
    }
//    @ExceptionHandler({SQLException.class})
//    public ResponseResult<Void> mapperHandle(Throwable ex){
//        ResponseResult<Void> responseResult=new ResponseResult<>(ex);
//        if(ex instanceof SQLIntegrityConstraintViolationException){
//            // 5201违反sql约束
//            responseResult.setStatus(5201);
//        }
//        return responseResult;
//    }
}
