package com.mealmate.service;

import java.util.List;

import com.mealmate.dto.DishDTO;
import com.mealmate.dto.DishPageQueryDTO;
import com.mealmate.entity.Dish;
import com.mealmate.vo.DishVO;
import com.mealmate.result.PageResult;

public interface DishService {

    /**
     * Add a new dish
     * @param dishDTO
     */
    void addDish(DishDTO dishDTO);

    /**
     * Get dish list
     * @param dishPageQueryDTO
     * @return
     */
    PageResult getDishList(DishPageQueryDTO dishPageQueryDTO);

    /**
     * delete dish by ids
     * @param ids
     */
    void deleteByMultiId(List<Long> ids);

    /**
     * get dish by id
     * @param id
     * @return
     */
    DishVO getById(Long id);

    /**
     * update dish
     * @param dishDTO
     */
    void updateDish(DishDTO dishDTO);

    /**
     * update status
     * @param status
     * @param id
     */
    void updateStatus(Integer status, Long id);

    /**
     * get dish by category id
     * @param categoryId
     * @return
     */
    List<Dish> getDishByCategoryId(Long categoryId);

    /**
     * get dish list by category id
     * @param categoryId
     * @return
     */
    List<DishVO> getDishVO(Long categoryId);
}
