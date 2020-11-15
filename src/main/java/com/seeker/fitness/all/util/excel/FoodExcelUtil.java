package com.seeker.fitness.all.util.excel;

import com.seeker.fitness.all.entity.Food;
import com.seeker.fitness.all.util.ReadConfigFileUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * 提供excel的读写功能
 */
public class FoodExcelUtil {
    private static String[] head;
    private static String sheetName;
    //写在静态初始化块中，使得其被加载时都会读取配置文件
    static {
        System.out.println("调用了初始化块");
        //读取配置文件获取模版信息
        Map<String,Object> yamlMap=ReadConfigFileUtil.getYaml("ExcelHeadModel.yml");
        sheetName=String.valueOf(yamlMap.get("foodImportTemplate_sheet"));
        String headStr=String.valueOf(yamlMap.get("foodImportTemplate_head"));
        head = headStr.split(",");
    }

    /**
     * 读取Excel文件 返回其中Food
     *
     * @param workbook
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static List<Food> readerToList(Workbook workbook) throws IOException, InvalidFormatException {
        List<Food> foodList = new LinkedList<>();
//        workbook.getNumberOfFonts();//可获取工作表个数
//        "食物名","碳水","蛋白质","脂肪","膳食纤维","钠","热量","千焦","克数","种类"
        Sheet sheet = workbook.getSheetAt(0);//获得文件中某一个表
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Food food = new Food();
            Row row = sheet.getRow(i);
            try {
            food.setName(getCellToString(row.getCell(0)));
            food.setCarbohydrate(getCellToDoule(row.getCell(1)));
            food.setProtein(getCellToDoule(row.getCell(2)));
            food.setFat(getCellToDoule(row.getCell(3)));
            food.setFiber(getCellToDoule(row.getCell(4)));
            food.setSodium(getCellToDoule(row.getCell(5)));
            food.setKcal(getCellToDoule(row.getCell(6)));
            food.setKj(getCellToDoule(row.getCell(7)));
            food.setWeight(getCellToDoule(row.getCell(8)));
            food.setChType(getCellToString(row.getCell(9)));
            foodList.add(food);
            }catch (NumberFormatException e){
                //如果遇到格式转换错误 就把随便一个必录项设置为null 以保证该行不会被写入数据库
                food.setCarbohydrate(null);
                foodList.add(food);
            }
        }
        return foodList;
    }

    /**
     * 写出上传模版
     * @param outputStream
     */
    public static void writeMode(Map<String,String[]> map, OutputStream outputStream) {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //设置sheet页名称
        XSSFSheet xssfSheet = xssfWorkbook.createSheet(sheetName);
        //设置头部信息
        setHead(xssfSheet);
        //设置类型的下拉选
        String[] foodTypes=map.get("foodTypes");
        setXSSFValidation(xssfSheet,foodTypes,1,100,9,9);
        try {
            xssfWorkbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 整体写出
     *
     * @param object
     * @param outputStream
     * @throws NullPointerException
     * @throws IOException
     */
    public static void writer(Object object, OutputStream outputStream) throws NullPointerException, IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("foods");
        try {
            writerInfo(object, sheet);
            //写出到流
            workbook.write(outputStream);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 按餐写出 每餐做统计以及一天统计
     *
     * @param outputStream
     * @throws NullPointerException
     * @throws IOException
     */
    public static void writerByMeal(Map meal, OutputStream outputStream) throws NullPointerException, IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("foods");
        double[] doublesAll = new double[100];
        Set<String> keySet = meal.keySet();
        for (String key : keySet) {
            Integer oldNum = sheet.getPhysicalNumberOfRows() == 0 ? 0 : sheet.getLastRowNum() + 1;
            sheet.createRow(oldNum).createCell(0).setCellValue(key);//设置餐名
            Object obj = meal.get(key);
            double[] d = writerInfo(obj, sheet);
            for (int i = 0; i < d.length; i++) {
                doublesAll[i] += d[i];
            }
        }
        //最后写出总计
        XSSFRow r = sheet.createRow(sheet.getLastRowNum() + 1);
        r.createCell(0).setCellValue("一天总计");
        r.createCell(1).setCellValue(doublesAll[1]);
        r.createCell(2).setCellValue(doublesAll[2]);
        r.createCell(3).setCellValue(doublesAll[3]);
        r.createCell(4).setCellValue(doublesAll[4]);
        r.createCell(5).setCellValue(doublesAll[5]);
        r.createCell(6).setCellValue(doublesAll[6]);
        r.createCell(7).setCellValue(doublesAll[7]);
        r.createCell(8).setCellValue(doublesAll[8]);
        try {
            //写出到流
            workbook.write(outputStream);
        } catch (IOException e) {
            throw e;
        }
    }


    /**
     * 写出一组，整体写出和按单元写出的基本方法
     *
     * @param object
     * @throws NullPointerException
     * @throws IOException
     */
    public static double[] writerInfo(Object object, XSSFSheet sheet) throws NullPointerException, IOException {
        //调用设置头部方法设置头部
        int headLength = setHead(sheet);
        //调用迭代方法去把所传对象的内容 最终汇总成一个list 里面装的必定是Food 如果最终汇总的list为空 则不进行写入
        List<Food> lists = objToList(object);
        if (lists.size() < 1) {
            throw new NullPointerException("所传入对象中没有Food类型！");
        }
        double[] doubles = new double[headLength];//用来装总记
        //循环写出food
        for (Food food : lists) {
            createCell(food, sheet, doubles);
        }
        //开始在总计行写出
        XSSFRow totalRow = sheet.createRow(sheet.getLastRowNum() + 1);
        totalRow.createCell(0).setCellValue("总计");
        totalRow.createCell(1).setCellValue(doubles[1]);
        totalRow.createCell(2).setCellValue(doubles[2]);
        totalRow.createCell(3).setCellValue(doubles[3]);
        totalRow.createCell(4).setCellValue(doubles[4]);
        totalRow.createCell(5).setCellValue(doubles[5]);
        totalRow.createCell(6).setCellValue(doubles[6]);
        totalRow.createCell(7).setCellValue(doubles[7]);
        totalRow.createCell(8).setCellValue(doubles[8]);
        sheet.createRow(sheet.getLastRowNum() + 1);
        return doubles;
    }

    /**
     * 该方法封装了 在一行中依次往后添加元素
     * @param food
     * @param sheet
     * @param doubles
     */
    private static void createCell(Food food, XSSFSheet sheet, double[] doubles) {
        XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);//sheet.getLastRowNum()方法用来获取该表中最后一行的下标+1就是在最后一行的下一行操作 相当于新增一行
        String name = food.getName();
        Double carbohydrate = food.getCarbohydrate();
        Double protein = food.getProtein();
        Double fat = food.getFat();
        Double fiber = food.getFiber();
        Double sodium = food.getSodium();
        Double kcal = food.getKcal();
        Double kj = food.getKj();
        Integer type = food.getType();
        String chType=food.getChType();
        Double weight = food.getWeight();
        row.createCell(0).setCellValue(String.valueOf(name));
        row.createCell(1).setCellValue(String.valueOf(carbohydrate));
        row.createCell(2).setCellValue(String.valueOf(protein));
        row.createCell(3).setCellValue(String.valueOf(fat));
        row.createCell(4).setCellValue(String.valueOf(fiber));
        row.createCell(5).setCellValue(String.valueOf(sodium));
        row.createCell(6).setCellValue(String.valueOf(kcal));
        row.createCell(7).setCellValue(String.valueOf(kj));
        row.createCell(8).setCellValue(String.valueOf(weight));
        String typeStr="";
        if(chType!=null){
            typeStr=chType;
        }else{
            typeStr=String.valueOf(type);
        }
        row.createCell(9).setCellValue(typeStr);
        double[] d = new double[doubles.length];
        if (carbohydrate != null) {
            d[1] = carbohydrate;
        }
        if (protein != null) {
            d[2] = protein;
        }
        if (fat != null) {
            d[3] = fat;
        }
        if (fiber != null) {
            d[4] = fiber;
        }
        if (sodium != null) {
            d[5] = sodium;
        }
        if (kcal != null) {
            d[6] = kcal;
        }
        if (kj != null) {
            d[7] = kj;
        }
        if (weight != null) {
            d[8] = weight;
        }

        for (int i = 0; i < d.length; i++) {
            doubles[i] += d[i];
        }
    }

    /**
     * 将传入对象转换成list的方法
     * @param object
     * @return
     */
    private static List<Food> objToList(Object object) {
        List<Food> list = new ArrayList<Food>();
        if (object != null&&object instanceof Food) {
            list.add((Food)object);
        }else if (object != null && object instanceof List) {
            List lf = (List)object;
            for (Object obj : lf) {
                if (obj instanceof Food) {
                    list.add((Food) obj);
                } else {
                    List<Food> l = objToList(obj);
                    for (Food f : l) {
                        list.add(f);
                    }
                }
            }
        }else if (object != null && object instanceof Map) {
            Map map = (Map)object;
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Object obj = entry.getValue();
                if (obj instanceof Food) {
                    list.add((Food) obj);
                } else {
                    List<Food> l = objToList(obj);
                    for (Food f : l) {
                        list.add(f);
                    }
                }
            }
        }
            return list;
    }
    /**
     * //设置类型的下拉选方法！！！！！！！
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     * @param sheet  模板sheet页（需要设置下拉框的sheet）
     * @param  textlist 下拉框显示的内容
     * @param  firstRow  添加下拉框对应开始行
     * @param  endRow    添加下拉框对应结束行
     * @param  firstCol  添加下拉框对应开始列
     * @param  endCol    添加下拉框对应结束列
     * @return XSSFSheet 设置好的sheet.
     */
    private static XSSFSheet setXSSFValidation(XSSFSheet sheet, String[] textlist, int firstRow, int endRow, int firstCol, int endCol){
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow,endRow, firstCol, endCol);
        // 加载下拉列表内容
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper
                .createExplicitListConstraint(textlist);
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
        validation.setSuppressDropDownArrow(true);
        validation.setShowErrorBox(true);
        // 数据有效性对象
        sheet.addValidationData(validation);
        return sheet;
    }

    /**
     * 设置头部方法
     * @param sheet
     * @return
     */
    private static int setHead(XSSFSheet sheet) {
        Integer oldNum = sheet.getPhysicalNumberOfRows();//已有行数
        //下面这个语句的意思为 创建一个row 该row表示该工作表中第一行 也就是设置该表的列名
        XSSFRow headRow = sheet.createRow(oldNum == 0 ? 0 : sheet.getLastRowNum() + 1);//设置头部标题
        for (int i = 0; i < head.length; i++) {//循环设置头部
            headRow.createCell(i).setCellValue(head[i]);
        }
        return head.length;
    }

    /**
     * 将Cell数据转换为double类型
     * @param cell
     * @return
     */
    public static Double getCellToDoule(Cell cell){
        String str=getCellToString(cell);
        if("".equals(str)){
            return null;
        }else {
            try {
                return Double.parseDouble(str);
            }catch (Exception e){
                e.printStackTrace();
                throw e;
            }
        }

    }
    public static String getCellToString(Cell cell){
        // 单元格内容
        String value = "";
        // 如果单元格为空或者单元格里面的数据为空则返回
        if ((cell == null) || cell.equals(null) || (cell.getCellType() == Cell.CELL_TYPE_BLANK)) {
            value = "";
        } else {
            // 判断数据类型
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_FORMULA:
                    value = "" + cell.getCellFormula();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    value = "" + cell.getNumericCellValue();
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = cell.getStringCellValue();
                    break;
                default:
                    break;
            }
        }
        return value;
    }
}
