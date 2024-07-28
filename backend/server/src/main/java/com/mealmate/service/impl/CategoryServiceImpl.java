package com.mealmate.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mealmate.constant.MessageConstant;
import com.mealmate.constant.StatusConstant;
import com.mealmate.dto.CategoryDTO;
import com.mealmate.dto.CategoryPageQueryDTO;
import com.mealmate.entity.Category;
import com.mealmate.exception.DeletionNotAllowedException;
import com.mealmate.mapper.CategoryMapper;
import com.mealmate.mapper.DishMapper;
import com.mealmate.mapper.MealMapper;
import com.mealmate.result.PageResult;
import com.mealmate.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private MealMapper mealMapper;

    /**
     * add new category
     *
     * @param categoryDTO
     */
    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();

        BeanUtils.copyProperties(categoryDTO, category);

        category.setStatus(StatusConstant.DISABLE);

        categoryMapper.insert(category);
    }

    /**
     * get category list
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult getCategoryList(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * delete category by id
     *
     * @param id
     */
    @Override
    public void deleteUsingId(Long id) {
        // Check if the current category is associated with any dishes, and throw a business exception if it is
        Integer dishCount = dishMapper.countByCategoryId(id);
        if (dishCount > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        // Check if the current category is associated with any meals, and throw a business exception if it is
        Integer mealCount = mealMapper.countByCategoryId(id);
        if (mealCount > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_MEAL);
        }

        // Delete the category data
        categoryMapper.deleteById(id);
    }

    /**
     * update category
     *
     * @param categoryDTO
     */
    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        categoryMapper.update(category);
    }

    /**
     * change category status
     *
     * @param status
     * @param id
     */
    @Override
    public void changeCategoryStatus(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .build();
        categoryMapper.update(category);
    }

    /**
     * get category using type
     *
     * @param type
     * @return
     */
    @Override
    public List<Category> getCategoryUsingType(Integer type) {
        return categoryMapper.getByType(type);
    }
}
