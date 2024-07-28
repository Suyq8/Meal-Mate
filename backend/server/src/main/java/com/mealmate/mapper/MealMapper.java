package com.mealmate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;
import com.mealmate.annotation.AutoFill;
import com.mealmate.dto.MealPageQueryDTO;
import com.mealmate.entity.Meal;
import com.mealmate.enumeration.OperationType;
import com.mealmate.vo.DishItemVO;
import com.mealmate.vo.MealVO;

@Mapper
public interface MealMapper {

    @Select("select count(id) from meal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    @AutoFill(OperationType.UPDATE)
    void update(Meal meal);

    @AutoFill(OperationType.INSERT)
    void insert(Meal meal);

    Page<MealVO> pageQuery(MealPageQueryDTO mealPageQueryDTO);

    @Select("select * from meal where id = #{id}")
    Meal getById(Long id);

    void deleteMulti(List<Long> mealIds);

    List<Meal> getByMeal(Meal meal);

    List<DishItemVO> getDishItemByMealId(Long mealId);

    @Select("select count(id) from meal where status = #{status}")
    Integer countByStatus(Integer status);
}
