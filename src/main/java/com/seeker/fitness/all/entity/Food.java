package com.seeker.fitness.all.entity;

import java.util.Objects;

/**
 * food_table表的实体类 该表并没有weight字段 因为业务所需所以在实体类中增加该属性
 */
public class Food {
    private static final String FORMAT="%.4f";
    private Integer fid;
    private String name;//食物名
    private Double carbohydrate;//碳水化合物
    private Double protein;//蛋白质
    private Double fat;//脂肪
    private Double fiber;//膳食纤维
    private Double sodium;//钠
    private Double kcal;//大卡
    private Double kj;//千焦
    private Integer type;//种类
    private String chType;//种类中文名
    private Double weight;//重量 单位:克

    public boolean isMust(){
        return  (getName()!=null&&!"".equals(getName())&&!"null".equals(getName()))
                &&getCarbohydrate()!=null
                &&getProtein()!=null
                &&getFat()!=null
                &&getWeight()!=null;
    }

    /**
     * 将当前对象所有成分设置为每一克
     */
    public void attributeToOne(){
        attrbuteFormat();
        if(getCarbohydrate()!=null){
            this.carbohydrate/=this.weight;
        }
        if(getProtein()!=null){
            this.protein/=this.weight;
        }
        if(getFat()!=null){
            this.fat/=this.weight;
        }
        if(getFiber()!=null){
            this.fiber/=this.weight;
        }
        if(getSodium()!=null){
            this.sodium/=this.weight;
        }
        if(getCarbohydrate()!=null&&getProtein()!=null&&getFat()!=null){
            this.kcal=this.protein*4+this.carbohydrate*4+this.fat*9;
            this.kj=this.kcal*4.1;
        }
        attrbuteFormat();
    }

    /**
     * 将所有Doule成分数值保留指定位数
     */
    private  void attrbuteFormat(){
//        DecimalFormat df=new DecimalFormat("#.000");
//        df.format(this.protein);

        if(this.carbohydrate!=null){
            this.carbohydrate= attrbuteFormat(this.carbohydrate);
        }
        if(this.protein!=null){
            this.protein= attrbuteFormat(this.protein);
        }
        if(this.fat!=null){
            this.fat= attrbuteFormat(this.fat);
        }
        if(this.fiber!=null){
            this.fiber= attrbuteFormat(this.fiber);
        }
        if(this.sodium!=null){
            this.sodium= attrbuteFormat(this.sodium);
        }
        if(this.kcal!=null){
            this.kcal= attrbuteFormat(this.kcal);
        }
        if(this.kj!=null){
            this.kj= attrbuteFormat(this.kj);
        }
    }
    private Double attrbuteFormat(Double d){
        if(d!=null){
            return Double.parseDouble(String.format(FORMAT,d));
        }else {
            return null;
        }
    }

    public void appoint(String name,Double carbohydrate,Double protein,Double fat,Double fiber,Double sodium,Integer type){
        setName(name);
        setCarbohydrate(carbohydrate);
        setProtein(protein);
        setFat(fat);
        setFiber(fiber);
        setSodium(sodium);
        setKcal(carbohydrate+protein*4+fat*9);
        setKj(getKcal()*4.1);
        setType(type);
        attrbuteFormat();
    }
    public void foodSet(Number g){
        this.weight=g.doubleValue();
        this.carbohydrate=this.weight*carbohydrate;
        this.protein=this.weight*protein;
        this.fat=this.weight*fat;
        this.fiber=this.weight*fiber;
        this.sodium=this.weight*sodium;
        this.kcal=this.protein*4+this.carbohydrate*4+this.fat*9;
        this.kj=this.kcal*4.1;
        attrbuteFormat();
    }

    public void foodAddition(){//食物加1克
        this.weight+=1;
        foodSet(this.weight);
    }
    public void foodAddition(Integer w){//食物加指定克
        this.weight+=w;
        foodSet(this.weight);
    }
    protected void foodInfos(){
        System.out.println("食物编号:"+getFid());
        System.out.println("食物名:"+getName());
        System.out.println("蛋白质:"+getProtein()+"g");
        System.out.println("碳水:"+getCarbohydrate()+"g");
        System.out.println("脂肪:"+getFat()+"g");
        System.out.println("膳食纤维:"+getFiber()+"g");
        System.out.println("钠:"+getSodium()+"mg");
        System.out.println("食物总热量:"+getKcal()+"kcal");
        System.out.println("食物总热量:"+getKj()+"kj");
        System.out.println("食物类型:"+getType());
        System.out.println("重量:"+getWeight()+"g");
        System.out.println("---------------------------------");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return Objects.equals(fid, food.fid) &&
                Objects.equals(name, food.name) &&
                Objects.equals(protein, food.protein) &&
                Objects.equals(carbohydrate, food.carbohydrate) &&
                Objects.equals(fat, food.fat) &&
                Objects.equals(fiber, food.fiber) &&
                Objects.equals(sodium, food.sodium) &&
                Objects.equals(weight, food.weight) &&
                Objects.equals(kcal, food.kcal) &&
                Objects.equals(kj, food.kj) &&
                Objects.equals(type, food.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fid, name, protein, carbohydrate, fat, fiber, sodium, weight, kcal, kj, type);
    }

    @Override
    public String toString() {
        return "Food{" +
                "fid=" + fid +
                ", name='" + name + '\'' +
                ", protein=" + protein +
                ", carbohydrate=" + carbohydrate +
                ", fat=" + fat +
                ", fiber=" + fiber +
                ", sodium=" + sodium +
                ", weight=" + weight +
                ", kcal=" + kcal +
                ", kj=" + kj +
                ", type=" + type +
                '}';
    }

    public void setWeight(Double weight) {
        this.weight = attrbuteFormat(weight);
    }

    public Integer getFid() {
        return fid;
    }

    public String getName() {
        return name;
    }

    public Double getProtein() {
        return protein;
    }

    public Double getCarbohydrate() {
        return carbohydrate;
    }

    public Double getFat() {
        return fat;
    }

    public Double getFiber() {
        return fiber;
    }

    public Double getSodium() {
        return sodium;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getKcal() {
        return kcal;
    }

    public Double getKj() {
        return kj;
    }

    public Integer getType() {
        return type;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProtein(Double protein) {
        this.protein = attrbuteFormat(protein);
    }

    public void setCarbohydrate(Double carbohydrate) {
        this.carbohydrate = attrbuteFormat(carbohydrate);
    }

    public void setFat(Double fat) {
        this.fat = attrbuteFormat(fat);
    }

    public void setFiber(Double fiber) {
        this.fiber = attrbuteFormat(fiber);
    }

    public void setSodium(Double sodium) {
        this.sodium = attrbuteFormat(sodium);
    }

    public void setKcal(Double kcal) {
        this.kcal = attrbuteFormat(kcal);
    }

    public void setKj(Double kj) {
        this.kj = attrbuteFormat(kj);
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getChType() {
        return chType;
    }

    public void setChType(String chType) {
        this.chType = chType;
    }
}
