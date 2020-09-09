package com.seeker.fitness.all.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.entity.Food;
import com.seeker.fitness.all.ex.InputAnomalyException;
import com.seeker.fitness.all.ex.WithIOException;
import com.seeker.fitness.all.mapper.FoodMapper;
import com.seeker.fitness.all.service.FoodCustomerService;
import com.seeker.fitness.all.util.excel.FoodExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
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
        String name=requestbody.getString("name");
        if(name==null){
            throw new InputAnomalyException("入参name不得为空！");
        }
        List<Food> list=foodMapper.getFoodsByName(name);
        return list;
    }

    /**
     * 下载食谱Excel文件(按餐写出)
     *
     * @param response
     */
    public void downLoadExcelService(Map requestbody, HttpServletResponse response){
        try {
            response.setContentType("application/force-download");//设置该参数 客户端接收后会下载 并不会直接打开
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("测试.xlsx", "ISO-8859-1"));
            FoodExcelUtil.writerByMeal(requestbody, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new WithIOException("获取IO流异常！，下载食谱文件失败！");
        }
    }

}
