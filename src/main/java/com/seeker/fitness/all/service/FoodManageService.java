package com.seeker.fitness.all.service;

import com.seeker.fitness.all.entity.Food;
import com.seeker.fitness.all.util.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 本接口定义了业务层食物后台管理的相关方法
 */
public interface FoodManageService {
    /**
     * 新增一种食物
     * @param food
     */
    void addFoodService(Food food);

    /**
     * 根据上传excel批量新增食物
     * @param multipartFile
     */
    ResponseResult addFoodsByExcelService(MultipartFile multipartFile);

    /**
     * 下载上传模版
     * @param response
     */
    void downLoadModeService(HttpServletResponse response);
}
