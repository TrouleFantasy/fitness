package com.seeker.fitness.all.mapper;

import com.seeker.fitness.all.entity.Food;
import com.seeker.fitness.all.entity.FoodType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FoodMapper {
    /**
     * 查看所有食物
     * @return 返回食物结果集
     */
    @Select("SELECT * FROM food_table")
    List<Food> getAll();

    /**
     * 根据种类查询食物
     * @param type
     * @return
     */
    @Select("SELECT * FROM food_table WHERE type=${type}")
    List<Food> getFoodsByType(Integer type);
    /**
     * 根据名称查询食物
     * @param name
     * @return
     */
    @Select("SELECT * FROM food_table WHERE name like '%${name}%'")
    List<Food> getFoodsByName(String name);

    /**
     * 根据type返回foodType对象
     * @param type
     * @return
     */
    @Select("SELECT * FROM food_type WHERE type=${type}")
    @Results({
            @Result(property = "chType",column = "ch_type"),
            @Result(property = "upType",column = "up_type"),
            @Result(property = "typeDescribe",column = "type_describe")
    })
    FoodType getFoodTypeByType(Integer type);

    /**
     * 查询全量type
     * @return
     */
    @Select("SELECT * FROM food_type WHERE type!=0")
    @Results({
            @Result(property = "chType",column = "ch_type"),
            @Result(property = "upType",column = "up_type"),
            @Result(property = "typeDescribe",column = "type_describe")
    })
    List<FoodType> getFoodType();

    /**
     * 根据ch_type返回foodType对象
     * @param chType
     * @return
     */
    @Select("SELECT * FROM food_type WHERE ch_type='${chType}'")
    @Results({
            @Result(property = "chType",column = "ch_type"),
            @Result(property = "upType",column = "up_type")
    })
    FoodType getFoodTypeByChType(String chType);

    /**
     * 根据指定食物id返回对应食物
     * @param fid
     * @return
     */
    @Select("SELECT * FROM food_table WHERE fid=#{fid}")
    Food getFood(Integer fid);

    /**
     * 根据指定食物名返回 对应模糊查询的食物
     * @param name
     * @return
     */
    @Select("SELECT * FROM food_table WHERE name LIKE CONCAT('%',#{name},'%')")
    List<Food> getFoodByName(String name);

    /**
     * 新增一种食物
     * @param food
     */
    @Insert("INSERT INTO food_table(name,carbohydrate,protein,fat,fiber,sodium,kcal,kj,type) VALUES(#{name},#{carbohydrate},#{protein},#{fat},#{fiber},#{sodium},#{kcal},#{kj},#{type})")
    Integer addFood(Food food);

}
