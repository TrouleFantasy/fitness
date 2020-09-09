package com.seeker.fitness.all.contrller;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.entity.Food;
import com.seeker.fitness.all.service.FoodCustomerService;
import com.seeker.fitness.all.service.FoodManageService;
import com.seeker.fitness.all.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("foods")
public class FoodContrller {
    private Logger log= LoggerFactory.getLogger(FoodContrller.class);
    @Autowired
    FoodManageService foodManageService;
    @Autowired
    FoodCustomerService foodCustomerService;
    /**
     * 添加一种新食物
     * @param food
     * @return
     */
    @RequestMapping("addFood")
    public ResponseResult<Void> addFood(@RequestBody Food food){
        foodManageService.addFoodService(food);
        return ResponseResult.successResponse();
    }

    /**
     * 根据上传excel批量新增食物
     * @param multipartFile
     * @return
     */
    @RequestMapping("uploadFoodByExcel")
    public ResponseResult<Void> addFoodsByExcel(@RequestParam("file")MultipartFile multipartFile){
        return foodManageService.addFoodsByExcelService(multipartFile);
    }

    /**
     * 根据种类获取对应食物信息
     * @param requestbody
     * @return
     */
    @RequestMapping("queryFoodsByType")
    public ResponseResult<List> queryFoodsByType(@RequestBody JSONObject requestbody){
        return ResponseResult.successResponse(foodCustomerService.queryFoodsServiceByType(requestbody));
    }

    /**
     * 根据食物名称获取对应食物信息
     * @param requestbody
     * @return
     */
    @RequestMapping("queryFoodsByName")
    public ResponseResult<List> queryFoodsByName(@RequestBody JSONObject requestbody){
        return ResponseResult.successResponse(foodCustomerService.queryFoodsServiceByName(requestbody));
    }

    /**
     * 下载食谱Excel文件(按餐写出)
     * @param response
     */
    @RequestMapping("downLoadExcel")
    public void downLoadExcel(@RequestBody Map<String,List<Food>> requestbody, HttpServletResponse response){
        foodCustomerService.downLoadExcelService(requestbody,response);
    }

    /**
     * 下载上传模版
     * @param response
     * @return
     */
    @RequestMapping("downLoadMode")
    public void downLoadMode(HttpServletResponse response){
        foodManageService.downLoadModeService(response);
    }
}
