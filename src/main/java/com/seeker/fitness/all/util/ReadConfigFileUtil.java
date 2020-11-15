package com.seeker.fitness.all.util;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class ReadConfigFileUtil {

    /**
     * 读取yaml配置文件
     * 此种获取方式可以兼容环境的变化：Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath)
     * Thread.currentThread() --获取当前进程相关信息对象
     * getContextClassLoader() --获取当前环境的ClassLoader对象
     * getResourceAsStream(filePath) --用获取的ClassLoader对象调用此方法获取对应文件的输入流
     * @param filePath
     * @return
     */
    public static Map<String,Object> getYaml(String filePath){
        try(InputStream resourceAsStream=Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath)){
            Map<String,Object> yamlMap=null;
            Yaml yaml=new Yaml();
            yamlMap=yaml.load(resourceAsStream);
            return  yamlMap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取properties文件
     * 此种获取方式可以兼容环境的变化：Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath)
     * Thread.currentThread() --获取当前进程相关信息对象
     * getContextClassLoader() --获取当前环境的ClassLoader对象
     * getResourceAsStream(filePath) --用获取的ClassLoader对象调用此方法获取对应文件的输入流
     * @param filePath
     * @return
     */
    public  static Properties getProperties(String filePath){
        try(InputStream resourceAsStream=Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath)){
            Properties properties=new Properties();
            properties.load(resourceAsStream);
            return properties;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
