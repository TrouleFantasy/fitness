package com.seeker.fitness.all.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;

public class ResponseResult<T> {
    private Integer status;
    private String message;
    private T data;

    public ResponseResult(){}
    public ResponseResult(Throwable ex){
        this.message=ex.getMessage();
    }

    //错误 传入status与message
    public static ResponseResult errorResponse(int status,String message){
        ResponseResult responseResult=new ResponseResult();
        responseResult.setData(null);
        responseResult.setStatus(status);
        responseResult.setMessage(message);
        return responseResult;
    }
    public static ResponseResult errorResponse(int status, String message,String interfaceName,Logger log){
        ResponseResult responseResult=new ResponseResult();
        responseResult.setData(null);
        responseResult.setStatus(status);
        responseResult.setMessage(message);
        log.info(interfaceName+"反参："+ JSONObject.toJSONString(responseResult));
        return responseResult;
    }

    //错误 传入message
    public static ResponseResult errorResponse(String message){
        ResponseResult responseResult=new ResponseResult();
        responseResult.setData(null);
        responseResult.setStatus(500);
        responseResult.setMessage(message);
        return responseResult;
    }
    public static ResponseResult errorResponse(String message,String interfaceName,Logger log){
        ResponseResult responseResult=new ResponseResult();
        responseResult.setData(null);
        responseResult.setStatus(500);
        responseResult.setMessage(message);
        log.info(interfaceName+"反参："+ JSONObject.toJSONString(responseResult));
        return responseResult;
    }

    //成功 不传入
    public static ResponseResult successResponse(){
        ResponseResult responseResult=new ResponseResult();
        responseResult.setStatus(0);
        responseResult.setMessage("成功");
        return responseResult;
    }
    public static ResponseResult successResponse(String interfaceName,Logger log){
        ResponseResult responseResult=new ResponseResult();
        responseResult.setStatus(0);
        responseResult.setMessage("成功");
        log.info(interfaceName+"反参："+ JSONObject.toJSONString(responseResult));
        return responseResult;
    }

    //成功 传入data
    public static ResponseResult successResponse(Object object){
        ResponseResult responseResult=new ResponseResult();
        responseResult.setStatus(0);
        responseResult.setMessage("成功");
        responseResult.setData(object);
        return responseResult;
    }
    public static ResponseResult successResponse(Object object,String interfaceName,Logger log){
        ResponseResult responseResult=new ResponseResult();
        responseResult.setStatus(0);
        responseResult.setMessage("成功");
        responseResult.setData(object);
        log.info(interfaceName+"反参："+ JSONObject.toJSONString(responseResult));
        return responseResult;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * 将当前对象转换为JSON字符串
     * @return
     */
    public String toJSONString(){
        return JSONObject.toJSONString(this);
    }
}
