package com.seeker.fitness.all.contrller;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.service.TableInfoService;
import com.seeker.fitness.all.util.ResponseResult;
import com.seeker.fitness.all.util.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminContrller {
    @Autowired
    TableInfoService tableInfoService;

    /**
     * 获取表详情
     *
     * @return
     */
    @RequestMapping("getTableInfo")
    public ResponseResult getTableInfo(HttpServletRequest request, @RequestBody JSONObject requestbody) {
        String token = request.getHeader("token");
        Token tokenObj = Token.parseTokenObj(token);
        String userCode = tokenObj.getUserCode();
        String keepsake = requestbody.getString("keepsake");
        String requestUserCode = requestbody.getString("userCode");
        String tableName = requestbody.getString("tableName");
        //判断登陆用户与所使用用户是同一个
        if (!userCode.equals(requestUserCode)) {
            return ResponseResult.errorResponse("用户非法！");
        }
        //判断是否是用户fitnessAdmin
        if (!"fitnessAdmin".equals(userCode)) {
            return ResponseResult.errorResponse("用户无权限！");
        } else if (!"王中王的火腿肠".equals(keepsake)) {
            return ResponseResult.errorResponse("凭证信物不正确，请联系管理员！");
        }
        //添加不显示字段
        List<String> exlude = new ArrayList<>();
        if ("user_list".equals(tableName)) {
            exlude.add("salt");
            exlude.add("password");
        }
        return tableInfoService.getTableInfo(request, requestbody, tableName, exlude);
    }

    @RequestMapping("queryTableList")
    public ResponseResult queryTableList(HttpServletRequest request, @RequestBody JSONObject requestbody) {
        try {
            String token = request.getHeader("token");
            Token tokenObj = Token.parseTokenObj(token);
            String userCode = tokenObj.getUserCode();
            String requestUserCode = requestbody.getString("userCode");
            //判断登陆用户与所使用用户是同一个
            if (!userCode.equals(requestUserCode)) {
                return ResponseResult.errorResponse("用户非法！");
            }
            if (!"fitnessAdmin".equals(userCode)) {
                return ResponseResult.errorResponse("用户无权限！");
            }
            //获取所有允许查看的表名
            return tableInfoService.queryTableList();
        } catch (Exception e) {
            return ResponseResult.errorResponse("系统错误，请联系管理员！");
        }
    }

}
