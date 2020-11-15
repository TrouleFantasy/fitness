package com.seeker.fitness.all.service.impl;

import com.seeker.fitness.all.entity.Food;
import com.seeker.fitness.all.entity.FoodType;
import com.seeker.fitness.all.ex.AddFoodException;
import com.seeker.fitness.all.ex.WithIOException;
import com.seeker.fitness.all.mapper.FoodMapper;
import com.seeker.fitness.all.service.FoodManageService;
import com.seeker.fitness.all.util.ResponseResult;
import com.seeker.fitness.all.util.excel.FoodExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 本类实现了关于食物后台管理业务层接口(FoodManageService)的所有方法
 */
@Service
public class FoodManageServiceImpl implements FoodManageService {
    private Logger log= LoggerFactory.getLogger(FoodManageServiceImpl.class);
    @Autowired
    private FoodMapper foodMapper;

    /**
     * 新增一种食物
     *
     * @param food
     */
    public void addFoodService(Food food) {
        Food foodObj = new Food();
        foodObj.appoint(food.getName(), food.getCarbohydrate(), food.getProtein(), food.getFat(), food.getFiber(), food.getSodium(), food.getType());
        try {
            foodMapper.addFood(foodObj);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AddFoodException("新增食物失败！sql执行不成功！");
        }

    }

    /**
     * 根据上传excel批量新增食物
     * @param multipartFile
     * @return
     */
    public ResponseResult addFoodsByExcelService(MultipartFile multipartFile) {
        try {
            String fileName = multipartFile.getOriginalFilename();
            if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
                String error = "上传文件格式不正确";
                return ResponseResult.errorResponse(error);
            }
            Workbook workbook = null;
            InputStream is = multipartFile.getInputStream();
            if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(is);
            } else {
                workbook = new HSSFWorkbook(is);
            }
            StringBuffer sb=new StringBuffer();
            List<Food> list= FoodExcelUtil.readerToList(workbook);
            //开始循环写入
            for(int i=0;i<list.size();i++){
                Food food=list.get(i);
                String foodName=food.getName();
                if(!food.isMust()){
                    if(foodName==null||"".equals(foodName)){
                        sb.append("第"+(i+2)+"行食物名为空！不可添加!|");
                    }else {
                        sb.append("第"+(i+2)+"行食物<"+foodName+">添加失败，缺少必录项，或格式不正确！|");
                    }
                    continue;
                }
                FoodType foodType=foodMapper.getFoodTypeByChType(food.getChType());
                if(foodType==null){
                    food.setType(16);
                }else {
                    food.setType(foodType.getType());
                }
                food.attributeToOne();//此方法将食物成分全部设置为每1克
                try {
                    foodMapper.addFood(food);//开始写入
                } catch (Exception e) {
                    sb.append("第"+(i+2)+"行食物<"+foodName+">添加失败，sql执行不成功!|");
                    e.printStackTrace();
                }
            }
            //判断sb字符串，假如该字符串长度不为零 说明有未录项
            if(sb.length()!=0){
                sb.deleteCharAt(sb.lastIndexOf("|"));
               throw new AddFoodException(sb.toString());
            }
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
            return ResponseResult.errorResponse("文件读取失败！");
        }
        return ResponseResult.successResponse();
    }





    /**
     * 下载上传模版
     * @param response
     */
    public void downLoadModeService(HttpServletResponse response) {
        String interfaceName="下载上传模版";
        System.out.println(interfaceName+"接口开始调用");
        try {
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("食物导入模版.xlsx", "UTF-8"));
            List<FoodType> l=foodMapper.getFoodType();
            String[] foodTypes=new String[l.size()];
            for(int i=0;i<l.size();i++){
                foodTypes[i]=l.get(i).getChType();
            }
            Map<String,String[]> map=new HashMap<>();
            map.put("foodTypes",foodTypes);
            FoodExcelUtil.writeMode(map,response.getOutputStream());
            System.out.println(interfaceName+"调用结束");
        } catch (IOException e) {
            e.printStackTrace();
            throw new WithIOException("获取IO流异常！下载模版失败！");
        }
    }
}
