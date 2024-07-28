package com.mealmate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.mealmate.entity.MealDish;

@Mapper
public interface MealDishMapper {

    /**
     * Get meal ids by dish id
     * @param dishIds
     * @return
     */
    List<Long> getMealIdsByDishIds(List<Long> dishIds);

    void insertMulti(List<MealDish> mealDishes);

    void deleteByMealIds(List<Long> mealIds);

    @Select("select * from meal_dish where meal_id = #{mealId}")
    List<MealDish> getByMealId(Long mealId);

    @Select("select count(md.id) from meal_dish md join dish d on md.dish_id=d.id where md.meal_id = #{mealId} and d.status = #{status}")
    Integer countByMealIdAndStatus(Long mealId, Integer status);
}
