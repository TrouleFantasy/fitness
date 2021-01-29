package com.seeker.fitness.all.service;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.util.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface TableInfoService {
    /**
     * 获取用户表详情
     * @return
     */
    ResponseResult getTableInfo(HttpServletRequest request, JSONObject requestbody, String tableName, List<String> exclude);

    /**
     * 获取允许查看的表详情
     * @return
     */
    ResponseResult queryTableList();
}
