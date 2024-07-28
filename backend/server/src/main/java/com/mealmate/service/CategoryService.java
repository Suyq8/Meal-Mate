package com.mealmate.service;

import java.util.List;

import com.mealmate.dto.CategoryDTO;
import com.mealmate.dto.CategoryPageQueryDTO;
import com.mealmate.entity.Category;
import com.mealmate.result.PageResult;

public interface CategoryService {
    
    /**
     * add new category
     * @param categoryDTO
     */
    void addCategory(CategoryDTO categoryDTO);

    /**
     * get category list
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult getCategoryList(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * delete category using id
     * @param id
     */
    void deleteUsingId(Long id);

    /**
     * update category
     * @param categoryDTO
     */
    void updateCategory(CategoryDTO categoryDTO);

    /**
     * change category status
     * @param status
     * @param id
     */
    void changeCategoryStatus(Integer status, Long id);

    /**
     * get category using id
     * @param type
     * @return
     */
    List<Category> getCategoryUsingType(Integer type);
}
