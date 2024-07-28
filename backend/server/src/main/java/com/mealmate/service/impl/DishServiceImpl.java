package com.mealmate.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mealmate.constant.MessageConstant;
import com.mealmate.constant.StatusConstant;
import com.mealmate.dto.DishDTO;
import com.mealmate.dto.DishPageQueryDTO;
import com.mealmate.entity.Dish;
import com.mealmate.entity.Flavor;
import com.mealmate.entity.Meal;
import com.mealmate.exception.DeletionNotAllowedException;
import com.mealmate.mapper.DishMapper;
import com.mealmate.mapper.FlavorMapper;
import com.mealmate.mapper.MealDishMapper;
import com.mealmate.mapper.MealMapper;
import com.mealmate.result.PageResult;
import com.mealmate.service.DishService;
import com.mealmate.vo.DishVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private FlavorMapper flavorMapper;
    @Autowired
    private MealDishMapper mealDishMapper;
    @Autowired
    private MealMapper mealMapper;

    @Transactional
    @Override
    public void addDish(DishDTO dishDTO) {
        log.info("addDish: {}", dishDTO);
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.insert(dish);

        Long dishId = dish.getId();

        List<Flavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            for (Flavor flavor : flavors) {
                flavor.setDishId(dishId);
            }
            flavorMapper.insertMulti(flavors);
        }
    }

    @Override
    public PageResult getDishList(DishPageQueryDTO dishPageQueryDTO) {
        log.info("getDishList: {}", dishPageQueryDTO);
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void deleteByMultiId(List<Long> ids) {
        log.info("delete dish by ids: {}", ids);

        // check if the dish is enabled
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.DELETE_DISH_NOT_ALLOWED);
            }
        }

        // check if the dish is related to meal
        List<Long> mealIds = mealDishMapper.getMealIdsByDishIds(ids);
        if (mealIds != null && !mealIds.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_MEAL);
        }

        dishMapper.deleteByMultiId(ids);
        flavorMapper.deleteByMultiId(ids);
    }

    @Override
    public DishVO getById(Long id) {
        log.info("get dish by id: {}", id);
        Dish dish = dishMapper.getById(id);

        List<Flavor> flavors = flavorMapper.getByDishId(id);

        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);

        return dishVO;
    }

    @Override
    public void updateDish(DishDTO dishDTO) {
        log.info("update dish: {}", dishDTO);
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.update(dish);

        Long dishId = dish.getId();

        List<Flavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavorMapper.deleteByDishId(dishId);
            for (Flavor flavor : flavors) {
                flavor.setDishId(dishId);
            }
            flavorMapper.insertMulti(flavors);
        }
    }

    @Transactional
    @Override
    public void updateStatus(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);

        // If it is a disable operation, we also need to disable the meal that include the current dish
        if (Objects.equals(status, StatusConstant.DISABLE)) {
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);

            List<Long> mealIds = mealDishMapper.getMealIdsByDishIds(dishIds);
            if (mealIds != null && !mealIds.isEmpty()) {
                for (Long mealId : mealIds) {
                    Meal meal = Meal.builder()
                            .id(mealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    mealMapper.update(meal);
                }
            }
        }
    }

    @Override
    public List<Dish> getDishByCategoryId(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.getByDish(dish);
    }

    @Override
    public List<DishVO> getDishVO(Long categoryId) {
        List<Dish> dishes = getDishByCategoryId(categoryId);
        List<DishVO> dishVOs = new ArrayList<>();

        for (Dish dish : dishes) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(dish, dishVO);

            List<Flavor> flavors = flavorMapper.getByDishId(dish.getId());

            dishVO.setFlavors(flavors);
            dishVOs.add(dishVO);
        }

        return dishVOs;
    }
}
