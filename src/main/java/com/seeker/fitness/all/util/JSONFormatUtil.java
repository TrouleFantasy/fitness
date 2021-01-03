package com.seeker.fitness.all.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 2020/4/21 张佳豪
 */
public class JSONFormatUtil {

    /**
     * xml转JSON 传入Docment
     * @param document
     * @return
     */
    public static String xmlToJson(Document document){
        return xmlToJson(document.asXML());
    }
    public static String xmlToJson(Document document,String keyWord){
        return xmlToJson(document.asXML(),keyWord);
    }
    public static String xmlToJson(String xml){
        String keyWord=null;
        return xmlToJson(xml,keyWord);
    }
    /**
     * xml转JSON 传入xml字符串
     * keyWord 入参 传入一个正则 用于利用标签名判断是否是数组 比如，标签名以List结尾就看做数组 传入正则".*?list|.*?List"
     * @param xml
     * @return
     */
    public static String xmlToJson(String xml,String keyWord){
        JSONObject jsonObject=new JSONObject();
        try {
            //将传入字符串转换成指定编码格式的字符串
//            String xmlString=new String(xml.getBytes("GBK"), Charset.forName("UTF-8"));
            Document doc= DocumentHelper.parseText(xml);
            doc.setXMLEncoding("GBK");
            Element root=doc.getRootElement();
            jsonObject.put(root.getName(),parseJsonObj(root,keyWord));
            return jsonObject.toJSONString();
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
//        catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return null;
//        }
    }
    /**
     * JSON转XML
     * 传入JSON字符串
     * @param jsonString
     * @return
     */
    public static String jsonToXml(String jsonString){
        return jsonToXml(JSONObject.parseObject(jsonString));
    }
    /**
     * JSON转XML
     * 传入JSON对象
     * @param jsonObject
     * @return
     */
    public static String jsonToXml(JSONObject jsonObject){
        Set<Map.Entry<String, Object>> entrySet=jsonObject.entrySet();
        if(entrySet.size()!=1){
            throw new RuntimeException("所传入对象，无唯一的根节点名称！");
        }
        String key=entrySet.iterator().next().getKey();
        Object value=entrySet.iterator().next().getValue();
        Document document=DocumentHelper.createDocument();
        document.setXMLEncoding("GBK");//设置字符集
        Element root=document.addElement(key);
        for(Object el:parseXml(value)){
            if(el instanceof Element){
                root.add((Element)el);
            }else{
                root.setText(String.valueOf(el));
            }
        }
        return document.asXML();

        //通过流的方式转换为字符串
//        try {
////            FileInputStream fis=new FileInputStream(new File("C:\\Users\\71402681\\Desktop\\share\\xml.txt"));
//            StringWriter sw = new StringWriter();
//            OutputFormat format = OutputFormat.createPrettyPrint();
//            format.setEncoding("GBK");
//            XMLWriter xmlWriter = new XMLWriter(sw, format);
//            xmlWriter.write(document);
//            return sw.toString();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    /**
     * 字符串编码转换
     * @param source
     * @param Target
     * @return
     */
    public static String encodeingString(String string,String source,String Target){
        try {
            byte[] bytes = new String(string.getBytes("ISO-8859-1"),source).getBytes(Target);
            return  new String(bytes,Target);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * json转xml迭代方法
     * @param object
     */
    private static List<Object> parseXml(Object object){
        List<Object> list=new LinkedList<>();
        if(object instanceof JSONObject){
            JSONObject jsonObject=(JSONObject)object;
            Set<Map.Entry<String, Object>> entrySet=jsonObject.entrySet();
            for(Map.Entry<String, Object> entry:entrySet){
                String key=entry.getKey();
                Object value=entry.getValue();
                //判断value是否是JSON如果是继续迭代 如果不是就为当前标签设置值
                if(value instanceof JSONObject){
                    Element element=DocumentHelper.createElement(key);
                    for(Object el:parseXml(value)){

                        element.add((Element)el);
                    }
                    list.add(element);
                }else if(value instanceof JSONArray){
                    for(Object el:parseXml(value)){
                        Element element=DocumentHelper.createElement(key);
                        if(el instanceof Element){
                            element.add((Element)el);
                        }else{
                            element.setText(String.valueOf(el));
                        }
                        list.add(element);
                    }
                }else{
                    Element element=DocumentHelper.createElement(key);
                    element.setText(String.valueOf(value));
                    list.add(element);
                }

            }
        }else if(object instanceof JSONArray){
            JSONArray array=(JSONArray)object;
            for(int i=0;i<array.size();i++){
                Object obj=array.get(i);
                //判断value是否是JSON如果是继续迭代 如果不是就为当前标签设置值
                if(obj instanceof JSON){
                    for(Object el:parseXml(obj)){
                        list.add(el);
                    }
                }else{
                    list.add(String.valueOf(obj));
                }

//                    JSONObject j=(JSONObject)obj;
//                    Set<Map.Entry<String, Object>> entrySet=j.entrySet();
//                    String key=entrySet.iterator().next().getKey();
//                    Object value=entrySet.iterator().next().getValue();
//                    element=DocumentHelper.createElement(key);
//                    //判断value是否是JSON如果是继续迭代 如果不是就为当前标签设置值
//                    if(isJSON(value)){
//                        element.setParent(parseXml(value));
//                    }else{
//                        element.setText(String.valueOf(value));
//                    }
            }
        }
        return list;
    }


    /**
     * XML转JSON迭代方法
     * @param sourceElement
     * @return
     */
    private static JSONObject parseJsonObj(Element sourceElement,String keyWord){
        JSONObject returnJson=new JSONObject();
        List<Element> elist=sourceElement.elements();
        if(elist.size()<1) {//成立说明传入标签无子标签 可获取其值
            returnJson.put(sourceElement.getName(),sourceElement.getText().trim());
            return  returnJson;
        }
        //创建一个Set集合 因为set集合自带去重功能
        Set<String> elSet=new LinkedHashSet<>();
        //循环取出所有标签名放到set集合中 结束后elSet将具有一个标签下所有独一无二的标签名
        for(Element element:elist){
            elSet.add(element.getName());
        }

        //开始根据标签名循环从传入标签中取出指定标签
        for(String name:elSet){
            List<Element> els=sourceElement.elements(name);
//            String resultStr=name.substring(name.length()-4>=0?name.length()-4:0);
            boolean isArr=false;
            if(null!=keyWord){
                if(name.matches(keyWord)){//成立说明是个数组 通过传入的正则判断数组成立条件
                    isArr=true;
                }
            }
            if(els.size()==1&&!isArr){//成立说明该name所对应的标签应该是一个对象或者最终包裹标签 不一定是集合
                Element element=els.get(0);

                if(element.elements().size()<1){//该判断成立说明该标签无子标签
                    returnJson.put(element.getName(),element.getText().trim());//添加到返回对象中
                }else {//否则说明该标签有标签 是一个对象
                    JSONObject resultJson=parseJsonObj(element,keyWord);
                    returnJson.put(name,resultJson);//添加到返回对象中
                }
            }else if(els.size()>1||isArr){//成立说明该name所对应的标签是一个集合
                JSONArray array=new JSONArray();
                String prevName="";
                for(Element elem:els){//循环对集合的每个对象做操作 并添加到准备好的数组中
                    prevName=elem.getName();
                    if(elem.elements().size()<1){//该判断成立说明该标签无子标签 也就是该集合不包含对象 要么直接包字符串要么为空
                        if(null==elem.getText()||"".equals(elem.getText())){//成立说明为空
                        }else{//否则直接包裹的字符
                            array.add(elem.getText());
                        }
                    }else {//
                        JSONObject jb=parseJsonObj(elem,keyWord);
                        Set<Map.Entry<String,Object>> entrySet=jb.entrySet();
                        Map.Entry<String,Object> entry=entrySet.iterator().next();//取出唯一的键值对
                        if(prevName.equals(entry.getKey())){//此判断为了兼容集合内承载的不是JSON的情况 列："arr3":["33","333","3333"]
                            array.add(entry.getValue());
                        }else{
                            array.add(jb);
                        }
                    }
                }
                returnJson.put(name,array);//添加到返回对象中
            }
        }
        return returnJson;
    }
}
