package com.seeker.fitness.mapper;

import com.seeker.fitness.all.entity.Food;
import com.seeker.fitness.all.entity.FoodType;
import com.seeker.fitness.all.mapper.FoodMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class FoodMapperTest {
    @Autowired
    private FoodMapper foodMapper;

    @Test
    void getAllTest(){
        List<Food> list=foodMapper.getAll();
        for (Food food:list){
            System.out.println(food.toString());
        }
    }
    @Test
    void getFoodTest(){
        Food food=foodMapper.getFood(2);
        System.out.println(food.toString());
    }

    @Test
    void getFoodBynameTest(){
        List<Food> list=foodMapper.getFoodsByName("鸡");
        for(Food food:list){
            System.out.println(food.toString());
        }
    }

    @Test
    void addFood(){
        Food food=new Food();
        food.appoint("鸡胸肉0",0.025,0.194,0.05,null,null,1,0,0,new Date(),new Date());
        foodMapper.addFood(food);
    }

    @Test
    void getFoodType(){
        FoodType foodType=foodMapper.getFoodTypeByType(0);
        System.out.println(foodType.toString());
    }
}
