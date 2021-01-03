package com.seeker.fitness.all.util;

import org.springframework.util.DigestUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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
     * 统一生成token的方法
     * @return
     */
    public static String createToken(String salt){
        String uid=UUID.nameUUIDFromBytes(salt.getBytes()).toString().replace("-","");
        String dateStr=new Date().toString();
        String allStr="fitness"+uid+salt+dateStr;
        String token= DigestUtils.md5DigestAsHex(allStr.getBytes());
        for(int i=0;i<10;i++){
            token=DigestUtils.md5DigestAsHex(token.getBytes());
        }
        return token;
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
     * 传入一个秒数s 返回s秒后的时间戳
     * @param s
     * @return
     */
    public static Long getTimeStamp(Long s){
        //当前时间戳 毫秒数
        Long nowTimeStampMS=new Date().getTime();
        //转换为秒
        Long nowTimeStampS=nowTimeStampMS/1000;
        //加上传入的秒数
        Long nowTimeStamp=nowTimeStampS+s;
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
