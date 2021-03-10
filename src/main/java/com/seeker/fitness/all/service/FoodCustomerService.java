package com.seeker.fitness.all.service;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.entity.Food;
import com.seeker.fitness.all.util.ResponseResult;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 本接口定义了用户相关的业务层食物方法
 */
public interface FoodCustomerService {
    /**
     * 根据种类获取对应食物信息
     * @param requestbody
     * @return
     */
    List<Food> queryFoodsServiceByType(JSONObject requestbody);

    /**
     * 根据食物名称获取对应食物信息
     * @param requestbody
     * @return
     */
    List<Food> queryFoodsServiceByName(JSONObject requestbody);
    /**
     * 下载食谱Excel文件(按餐写出)
     * @param response
     */
    ResponseResult downLoadExcelService(Map requestbody, HttpServletResponse response);
}
