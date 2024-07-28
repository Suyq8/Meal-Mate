package com.mealmate.service;

import java.util.List;

import com.mealmate.dto.MealDTO;
import com.mealmate.dto.MealPageQueryDTO;
import com.mealmate.entity.Meal;
import com.mealmate.result.PageResult;
import com.mealmate.vo.DishItemVO;
import com.mealmate.vo.MealVO;

public interface MealService {
    /**
     * Add a new meal
     * @param mealDTO
     */
    void addMeal(MealDTO mealDTO);

    /**
     * Get meal list
     * @param mealPageQueryDTO
     * @return
     */
    PageResult getMealList(MealPageQueryDTO mealPageQueryDTO);

    /**
     * Delete multiple meals
     * @param ids
     */
    void deleteMulti(List<Long> ids);

    /**
     * Get meal by id
     * @param id
     * @return
     */
    MealVO getById(Long id);

    /**
     * Update meal
     * @param mealDTO
     */
    void update(MealDTO mealDTO);

    /**
     * Update meal status
     * @param status
     * @param id
     */
    void updateStatus(Integer status, Long id);

    /**
     * Get meal by meal
     * @param meal
     * @return
     */
    List<Meal> getByMeal(Meal meal);

    /**
     * Get dish item by meal id
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}
