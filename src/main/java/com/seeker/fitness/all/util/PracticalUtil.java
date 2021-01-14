package com.seeker.fitness.all.util;

import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 按照规律生成打印日志的标示，辅助查找日志
 */
public class PracticalUtil {

    /**
     * 根据时间生成时间标志
     *
     * @return
     */
    public static String makeOnlyFlag() {
        String str = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("调用于yyyy-MM-dd HH:mm:ss:SSS");
        str = simpleDateFormat.format(new Date());
        return str;
    }

    /**
     * 通过身份证号计算周岁
     *
     * @param IDNumber
     * @return
     */
    public static Integer getAgeByIdNumber(String IDNumber) {
        Integer age = null;
        //将当前时间格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String nowDateStr = simpleDateFormat.format(new Date());
        //获取身份证中的出生年月日并做规整处理
        String oldDateStr = IDNumber.substring(6, 14);
        oldDateStr.trim();
        //将当前时间年月日转换成整型
        Integer nowDateInt = Integer.parseInt(nowDateStr.substring(0, 4));
        Integer nowDateIntM = Integer.parseInt(nowDateStr.substring(4, 6));
        Integer nowDateIntS = Integer.parseInt(nowDateStr.substring(6));
        //将身份证中时间年月日转换成整型
        Integer oldDateInt = Integer.parseInt(oldDateStr.substring(0, 4));
        Integer oldDateIntM = Integer.parseInt(oldDateStr.substring(4, 6));
        Integer oldDateIntS = Integer.parseInt(oldDateStr.substring(6));
        //初步确认年龄
        age = nowDateInt - oldDateInt;
        //进行是否满周岁判断
        if (nowDateIntM < oldDateIntM) {
            age -= 1;
        } else if (nowDateIntM == oldDateIntM) {
            if (nowDateIntS < oldDateIntS) {
                age -= 1;
            }
        }
        return age;
    }

    //-------------------------------------------------------------------------------------------------------------------
    /**
     * 根据出生日期计算出周岁-利用Date入参
     * @param birthDate
     * @return
     */
    public static Integer getAgeByBirthDate(Date birthDate){
        //将出生日期格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String birthDateStr=simpleDateFormat.format(birthDate);
        return getAgeByBirthDate(birthDateStr);
    }
    /**
     * 根据出生日期计算出周岁
     * @param birthDate
     * @return
     */
    public static Integer getAgeByBirthDate(String birthDate) {
        Integer age = null;
        //将当前时间格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String nowDateStr = simpleDateFormat.format(new Date());
        //去空格
        birthDate.trim();
        //将当前时间年月日转换成整型
        Integer nowDateInt = Integer.parseInt(nowDateStr.substring(0, 4));
        Integer nowDateIntM = Integer.parseInt(nowDateStr.substring(4, 6));
        Integer nowDateIntS = Integer.parseInt(nowDateStr.substring(6));
        //将身份证中时间年月日转换成整型
        Integer oldDateInt = Integer.parseInt(birthDate.substring(0, 4));
        Integer oldDateIntM = Integer.parseInt(birthDate.substring(4, 6));
        Integer oldDateIntS = Integer.parseInt(birthDate.substring(6));
        //初步确认年龄
        age = nowDateInt - oldDateInt;
        //进行是否满周岁判断
        if (nowDateIntM < oldDateIntM) {
            age -= 1;
        } else if (nowDateIntM == oldDateIntM) {
            if (nowDateIntS < oldDateIntS) {
                age -= 1;
            }
        }
        return age;
    }

    //-------------------------------------------------------------------------------------------------------------------

    /**
     * 将给定字符串用base64编码
     * @param source
     * @return
     */
    public static String base64Encoder(String source){
        try {
            Base64.Encoder encoder=Base64.getEncoder();
            byte[] sourceByte=source.getBytes("UTF-8");
            return encoder.encodeToString(sourceByte);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将给定字符串用base64解码
     * @param source
     * @return
     */
    public static String base64Decoder(String source){
        try{
            Base64.Decoder decoder=Base64.getDecoder();
            byte[] sourceByte=decoder.decode(source);
            return new String(sourceByte,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //-------------------------------------------------------------------------------------------------------------------

    /**
     * 获取客户端IP地址
     * @param request
     * @return
     */
    public static  String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    //-------------------------------------------------------------------------------------------------------------------

    /**
     * 制造安全的密码
     * @param password
     * @return
     */
    public static String createSecurePassword(String password,String salt){
        if(salt==null){
            salt="";
        }
        String readyPass=salt+password+salt;
        String MD5Password=DigestUtils.md5DigestAsHex(readyPass.getBytes());
        for(int i=0;i<10;i++){
            String md5Salt=salt+MD5Password+salt;
            MD5Password=DigestUtils.md5DigestAsHex(md5Salt.getBytes());
        }
        return MD5Password;
    }

    /**
     * 比对密码
     * @param password
     * @param resultPassword
     * @param salt
     * @return
     */
    public static boolean comparisonPassword(String resultPassword,String password,String salt){
        String md5Password=createSecurePassword(password,salt);
        if(resultPassword.equals(md5Password)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 制造盐值
     * @return
     */
    public static String createSecureSalt(){
        Random random=new Random();
        int s=random.nextInt(22);
        int e=s+10;
        return UUID.randomUUID().toString().replace("-","").substring(s,e);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * 将字符串数组转换为字符串
     * @param strArr
     * @param spaceMark
     * @return
     */
    public static String arrayToString(String[] strArr,String spaceMark){
        StringBuffer string=new StringBuffer();
        for(int i=0;i<strArr.length;i++){
            string.append(spaceMark).append(strArr[i]);
        }
        return string.toString().substring(spaceMark.length());
    }

    /**
     * 从前往后匹配一次符合给定条件的字符 匹配即停
     * @param regex
     * @param materials
     * @param inversion
     * @return
     */
    public static String headGetStringByRegex(String regex,String materials,boolean inversion){
        String result="";
        //制造匹配模版
        Pattern pattern=Pattern.compile(regex);
        //管理模版与被匹配字符串 返回一个Matcher
        Matcher matcher=pattern.matcher(materials);
        //从头开始匹配 匹配即停
        if(matcher.lookingAt()){
            //赋值给返回值
            result=matcher.group();
            //判断inversion 是否反转
            if(inversion){
                result=arrayToString(materials.split(result),"");
            }
        }
        return result;
    }

    /**
     * 循环匹配并返回符合给定条件的字符
     * @param regex
     * @param materials
     * @param inversion
     * @return
     */
    public static List<String> moreGetStringByRegex(String regex, String materials, boolean inversion){
        List<String> results=new ArrayList<>();
        //制造匹配模版
        Pattern pattern=Pattern.compile(regex);
        //管理模版与被匹配字符串 返回一个Matcher
        Matcher matcher=pattern.matcher(materials);
        //从头开始匹配 匹配即停
        while (matcher.find()){
            //获取到符合正则的字符串
            String result=matcher.group();
            //如果为true 则说明需要进行反转
            if(inversion){
                //获取到符合正则的字符串前面的字符
                String cutStr=materials.substring(0,materials.indexOf(result));
                //添加到返回集合中
                if(!"".equals(cutStr)&&cutStr!=null){
                    results.add(cutStr);
                }
                //将需要匹配的字符串进行剪短 主要是剪去已经匹配到的字符串以及已经添加到返回集合中的字符 如果不减将会出现结果性错误
                materials=materials.replace(cutStr+result,"");
            }else{
                //为false则直接添加进返回集合
                results.add(result);
            }
        }
        //如果是需要反转的且在最后一次循环的时候result在materials中并不处于最后 这个时候将不会进入while循环 也就是不会将最后所剩的字符串假如返回集合中 在这里判断加一下
        /*本取反时方法示例
            原字符串：aa00bb一二aa11bb三四五aa22bb六七aa33bb八九十

            materials值：
            aa00bb一二aa11bb三四五aa22bb六七aa33bb八九十
            一二aa11bb三四五aa22bb六七aa33bb八九十
            三四五aa22bb六七aa33bb八九十
            六七aa33bb八九十
            八九十
            ------------------
            最后取反输出的值：
            一二
            三四五
            六七
            八九十
        * */
        if(inversion){
            results.add(materials);
        }
        return results;
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * 获取当前时间戳
     * @return
     */
    public static Long getTimeStamp(){
        return new Date().getTime();
    }

    /**
     * 传入一个毫秒数ms 返回ms毫秒后的时间戳
     * @param ms
     * @return
     */
    public static Long getTimeStamp(Long ms){
        //当前时间戳 毫秒数
        Long nowTimeStampMS=new Date().getTime();
        //加上传入的秒数
        Long nowTimeStamp=nowTimeStampMS+ms;
        return nowTimeStamp;
    }


    //-------------------------------------------------------------------------------------------------------------------

    /**
     * 根据传入的文件路径读取回文件内容
     * @param path
     * @return
     */
    public static String rederFileData(String path){
        String result=null;
        StringBuilder sb=new StringBuilder();
        try (FileReader fileReader= new FileReader(path)){
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            String buff=null;
            while ((buff=bufferedReader.readLine())!=null){
                sb.append(buff);
            }
            result=sb.toString();
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return result;
        }
    }

    //-------------------------------------------------------------------------------------------------------------------

    /**
     * 传入文件路径以及需要写出的文件数据以写出文件
     * @param path
     * @param dataArr
     */
    public static void writeToFile(String path,byte[] dataArr){
        try(FileOutputStream fileOutputStream=new FileOutputStream(path);){
            fileOutputStream.write(dataArr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //-------------------------------------------------------------------------------------------------------------------

    /**
     * 判断指定时间是否在一个时间区间内
     * @return
     */
    public static boolean isTimeRange(Date date,String startTime,String endTime){
        boolean booleanVariable=false;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
        String nowDateStr=simpleDateFormat.format(date);
        Integer nowDateInt=Integer.parseInt(nowDateStr.substring(0,2));
        Integer startTimeInt=Integer.parseInt(startTime.substring(0,2));
        Integer endTimeInt=Integer.parseInt(endTime.substring(0,2));
        if(nowDateInt>startTimeInt&&nowDateInt<endTimeInt){//判断是否在 时之内
            booleanVariable=true;
        }else if(nowDateInt==startTimeInt){//如果等于开始 时 那么就去比较开始 分
            //比较分
            Integer nowDate_mm_Int=Integer.parseInt(nowDateStr.substring(3,5));
            Integer startTime_mm_Int=Integer.parseInt(startTime.substring(3,5));
            if(nowDate_mm_Int>startTime_mm_Int){//如果大于开始分
                booleanVariable=true;
            }else if(nowDate_mm_Int==startTime_mm_Int){//如果等与开始分
                //比较秒
                Integer nowDate_ss_Int=Integer.parseInt(nowDateStr.substring(6));
                Integer startTime_ss_Int=Integer.parseInt(startTime.substring(6));
                if(nowDate_ss_Int>=startTime_ss_Int){//如果大于等于开始秒
                    booleanVariable=true;
                }
            }
        }else if(nowDateInt==endTimeInt){//如果等于结束 时 那么就去比较结束 分
            //比较分
            Integer nowDate_mm_Int=Integer.parseInt(nowDateStr.substring(3,5));
            Integer endTime_mm_Int=Integer.parseInt(endTime.substring(3,5));
            if(nowDate_mm_Int<endTime_mm_Int){//如果小于结束分
                booleanVariable=true;
            }else if(nowDate_mm_Int==endTime_mm_Int){//如果等与结束分
                //比较秒
                Integer nowDate_ss_Int=Integer.parseInt(nowDateStr.substring(6));
                Integer endTime_ss_Int=Integer.parseInt(endTime.substring(6));
                if(nowDate_ss_Int<=endTime_ss_Int){//如果小于等于结束秒
                    booleanVariable=true;
                }
            }
        }
        return booleanVariable;
    }

    //-------------------------------------------------------------------------------------------------------------------

    public static String getExceptionMessageText(Exception e){
        StackTraceElement stackTraceElement=e.getStackTrace()[0];
        String fileName=stackTraceElement.getFileName();
        String className=stackTraceElement.getClassName();
        String methodName=stackTraceElement.getMethodName();
        String errorLine=stackTraceElement.getClassName();
        return fileName+"|"+className+"|"+methodName+"|"+errorLine;
    }

    //-------------------------------------------------------------------------------------------------------------------
}
