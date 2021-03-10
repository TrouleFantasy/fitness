package com.seeker.fitness.all.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.entity.Food;
import com.seeker.fitness.all.ex.InputAnomalyException;
import com.seeker.fitness.all.ex.WithIOException;
import com.seeker.fitness.all.mapper.fitnessmapper.FoodMapper;
import com.seeker.fitness.all.service.FoodCustomerService;
import com.seeker.fitness.all.util.ResponseResult;
import com.seeker.fitness.all.util.excel.FoodExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 本类实现了用户相关的食物业务层接口(FoodCustomerService)的所有方法
 */
@Service
public class FoodCustomerServiceImpl implements FoodCustomerService {
    private Logger log= LoggerFactory.getLogger(FoodCustomerServiceImpl.class);
    @Autowired
    private FoodMapper foodMapper;
    /**
     * 根据种类获取对应食物信息
     *
     * @param requestbody
     * @return
     */
    public List<Food> queryFoodsServiceByType(JSONObject requestbody) {
        String interfaceName="根据种类获取对应食物信息";
        log.info(interfaceName+"入参："+requestbody.toJSONString());
        Object obj=requestbody.get("type");
        if (obj == null || !(obj instanceof Integer)) {
            throw new InputAnomalyException("type类型有误!");
        }
        Integer type=(Integer)obj;
        List<Food> list = null;
        if (type == 0) {//如果type等于0 就是查所有食物不分种类
            list = foodMapper.getAll();
        } else {
            list = foodMapper.getFoodsByType(type);
        }
        log.info(interfaceName+"反参："+JSONObject.toJSONString(list));
        return list;
    }

    /**
     * 根据食物名称获取对应食物信息
     * @param requestbody
     * @return
     */
    public List<Food> queryFoodsServiceByName(JSONObject requestbody) {
        String interfaceName="根据食物名称获取对应食物信息";
        log.info(interfaceName+"入参："+requestbody.toJSONString());
        String name=requestbody.getString("name");
        if(name==null){
            throw new InputAnomalyException("入参name不得为空！");
        }
        List<Food> list=foodMapper.getFoodsByName(name);
        log.info(interfaceName+"反参："+JSONObject.toJSONString(list));
        return list;
    }

    /**
     * 下载食谱Excel文件(按餐写出)
     *
     * @param response
     */
    public ResponseResult downLoadExcelService(Map requestbody, HttpServletResponse response){
        String interfaceName="下载食谱Excel文件(按餐写出)";
        log.info(interfaceName+"入参："+JSONObject.toJSONString(requestbody));
        try {
//            response.setContentType("application/force-download");//设置该参数 客户端接收后会下载 并不会直接打开
////            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
//            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
//            response.setCharacterEncoding("UTF-8");
////            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("meal.xlsx", "ISO-8859-1"));
//            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("meal.xlsx", "UTF-8"));
            //创建承载数据的byte流
            ByteArrayOutputStream byteOutputStream=new ByteArrayOutputStream();
            FoodExcelUtil.writerByMeal(requestbody, byteOutputStream);
//            PracticalUtil.writeToFile("/Users/seeker/Desktop/qweqwe.xlsx",byteOutputStream.toByteArray());
            log.info(interfaceName+"设定为不打印反参");
            //返回成功以及数据
            return ResponseResult.successResponse(byteOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            throw new WithIOException("获取IO流异常！，下载食谱文件失败！");
        }
    }

}
