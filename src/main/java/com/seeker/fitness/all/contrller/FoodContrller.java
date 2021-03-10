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
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("foods")
public class FoodContrller {
    private Logger log= LoggerFactory.getLogger(FoodContrller.class);
    @Autowired
    FoodManageService foodManageService;
    @Autowired
    FoodCustomerService foodCustomerService;
    /**
     * 新增一种食物
     * @param food
     * @return
     */
    @RequestMapping("addFood")
    public ResponseResult<Void> addFood(@RequestBody Food food){
        String interfaceName="新增一种食物";
        log.info("<<--------------"+interfaceName+"调用开始-------------->>");
        ResponseResult responseResult = foodManageService.addFoodService(food);
        log.info("<<--------------"+interfaceName+"调用结束-------------->>");
        return responseResult;
    }

    /**
     * 根据上传excel批量新增食物
     * @param multipartFile
     * @return
     */
    @RequestMapping("uploadFoodByExcel")
    public ResponseResult<Void> addFoodsByExcel(@RequestParam("file")MultipartFile multipartFile,@RequestParam("userCode")String userCode){
        String interfaceName="根据上传excel批量新增食物";
        log.info("<<--------------"+interfaceName+"调用开始-------------->>");
        ResponseResult responseResult = foodManageService.addFoodsByExcelService(userCode,multipartFile);
        log.info("<<--------------"+interfaceName+"调用结束-------------->>");
        return responseResult;
    }

    /**
     * 根据种类获取对应食物信息
     * @param requestbody
     * @return
     */
    @RequestMapping("queryFoodsByType")
    public ResponseResult<List> queryFoodsByType(@RequestBody JSONObject requestbody){
        String interfaceName="根据种类获取对应食物信息";
        log.info("<<--------------"+interfaceName+"调用开始-------------->>");
        ResponseResult responseResult = ResponseResult.successResponse(foodCustomerService.queryFoodsServiceByType(requestbody));
        log.info("<<--------------"+interfaceName+"调用结束-------------->>");
        return responseResult;
    }

    /**
     * 根据食物名称获取对应食物信息
     * @param requestbody
     * @return
     */
    @RequestMapping("queryFoodsByName")
    public ResponseResult<List> queryFoodsByName(@RequestBody JSONObject requestbody){
        String interfaceName="根据食物名称获取对应食物信息";
        log.info("<<--------------"+interfaceName+"调用开始-------------->>");
        ResponseResult responseResult = ResponseResult.successResponse(foodCustomerService.queryFoodsServiceByName(requestbody));
        log.info("<<--------------"+interfaceName+"调用结束-------------->>");
        return responseResult;
    }

    /**
     * 下载食谱Excel文件(按餐写出)
     * @param response
     */
    @RequestMapping("downLoadExcel")
    public ResponseResult downLoadExcel(@RequestBody LinkedHashMap<String,List<Food>> requestbody, HttpServletResponse response){
        String interfaceName="下载食谱Excel文件(按餐写出)";
        log.info("<<--------------"+interfaceName+"调用开始-------------->>");
        ResponseResult responseResult = foodCustomerService.downLoadExcelService(requestbody,response);
        log.info("<<--------------"+interfaceName+"调用结束-------------->>");
        return responseResult;
    }

    /**
     * 下载上传模版
     * @param response
     * @return
     */
    @RequestMapping("downLoadMode")
    public void downLoadMode(HttpServletResponse response){
        String interfaceName="下载上传模版";
        log.info("<<--------------"+interfaceName+"调用开始-------------->>");
        foodManageService.downLoadModeService(response);
        log.info("<<--------------"+interfaceName+"调用结束-------------->>");
    }
}
