package com.seeker.fitness.all.util;

import com.alibaba.fastjson.JSONObject;

public class ResponseResult<T> {
    private Integer status;
    private String message;
    private T data;

    public ResponseResult(){}
    public ResponseResult(Throwable ex){
        this.message=ex.getMessage();
    }

    public static ResponseResult errorResponse(String message){
        ResponseResult responseResult=new ResponseResult();
        responseResult.setData(null);
        responseResult.setStatus(500);
        responseResult.setMessage(message);
        return responseResult;
    }

    public static ResponseResult successResponse(){
        ResponseResult responseResult=new ResponseResult();
        responseResult.setStatus(0);
        responseResult.setMessage("成功");
        return responseResult;
    }
    public static ResponseResult successResponse(Object object){
        ResponseResult responseResult=new ResponseResult();
        responseResult.setStatus(0);
        responseResult.setMessage("成功");
        responseResult.setData(object);
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
