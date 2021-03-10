package com.seeker.fitness.all.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.entity.ColumnInfo;
import com.seeker.fitness.all.mapper.fitnessmapper.QueryTableMapper;
import com.seeker.fitness.all.mapper.informationschemamapper.TableMapper;
import com.seeker.fitness.all.service.TableInfoService;
import com.seeker.fitness.all.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TableInfoServiceImpl implements TableInfoService {
    private static Logger log= LoggerFactory.getLogger(TableInfoServiceImpl.class);
    @Autowired
    private TableMapper tableMapper;

    @Autowired
    private QueryTableMapper queryTableMapper;

    /**
     * 开发人员专用接口-获取表详情
     * @return
     */
    public ResponseResult getTableInfo(HttpServletRequest request, JSONObject requestbody, String tableName, List<String> exclude) {
        String interfaceName="开发人员专用接口-获取表详情Service层";
        log.info(interfaceName+"入参："+ JSONObject.toJSONString(requestbody));
        try {
            //获取允许查询的表名
            List<String> resultAllowList=queryTableMapper.queryAllowTableName();
            if(!resultAllowList.contains(tableName)){
                return ResponseResult.errorResponse("未经允许的表！",interfaceName,log);
            }

            //创建返回对象
            JSONObject returnObj = new JSONObject();
            //设置表名
            returnObj.put("tableName", tableName);

            //设置要展示的列
            Map<String, String> tableHead = new LinkedHashMap<>();
            tableHead.put("COLUMN_NAME", "列名");
            tableHead.put("COLUMN_TYPE", "字段类型");
            tableHead.put("IS_NULLABLE", "是否为NULL");
            tableHead.put("COLUMN_COMMENT", "描述信息");
            returnObj.put("tableHead", tableHead);

            //设置数据
            List<ColumnInfo> responeArr = new ArrayList<>();
            returnObj.put("tableBody", responeArr);

            //查询指定表信息
            List<ColumnInfo> list = tableMapper.getTableInfo(tableName);
            for (ColumnInfo columnInfo : list) {
                //获取列名
                String columnName = columnInfo.getCOLUMN_NAME();
                //指定列信息不返回
                if (exclude.contains(columnName)) {
                    continue;
                }
                responeArr.add(columnInfo);
            }
            return ResponseResult.successResponse(returnObj,interfaceName,log);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResponse("系统错误，请联系管理员！",interfaceName,log);
        }
    }

    /**
     * 开发人员专用接口-获取允许查看的表名
     * @return
     */
    public ResponseResult queryTableList() {
        String interfaceName="开发人员专用接口-获取允许查看的表名";
        //查询Fitness库下所有允许查看的表名
        List<String> list=queryTableMapper.queryAllowTableName();
        return ResponseResult.successResponse(list,interfaceName,log);
    }
}
