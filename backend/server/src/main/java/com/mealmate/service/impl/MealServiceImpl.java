package com.mealmate.service.impl;

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
import com.mealmate.dto.MealDTO;
import com.mealmate.dto.MealPageQueryDTO;
import com.mealmate.entity.Meal;
import com.mealmate.entity.MealDish;
import com.mealmate.exception.DeletionNotAllowedException;
import com.mealmate.mapper.MealDishMapper;
import com.mealmate.mapper.MealMapper;
import com.mealmate.result.PageResult;
import com.mealmate.service.MealService;
import com.mealmate.vo.DishItemVO;
import com.mealmate.vo.MealVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MealServiceImpl implements MealService {

    @Autowired
    private MealMapper mealMapper;
    @Autowired
    private MealDishMapper mealDishMapper;

    @Transactional
    @Override
    public void addMeal(MealDTO mealDTO) {
        log.info("add meal: {}", mealDTO);
        Meal meal = new Meal();
        BeanUtils.copyProperties(mealDTO, meal);

        mealMapper.insert(meal);

        Long mealId = meal.getId();
        List<MealDish> mealDishes = mealDTO.getMealDishes();
        for (MealDish mealDish : mealDishes) {
            mealDish.setMealId(mealId);
        }

        mealDishMapper.insertMulti(mealDishes);
    }

    @Override
    public PageResult getMealList(MealPageQueryDTO mealPageQueryDTO) {
        PageHelper.startPage(mealPageQueryDTO.getPage(), mealPageQueryDTO.getPageSize());

        Page<MealVO> page = mealMapper.pageQuery(mealPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Transactional
    @Override
    public void deleteMulti(List<Long> ids) {
        for (Long id : ids) {
            Meal meal = mealMapper.getById(id);
            if (Objects.equals(meal.getStatus(), StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.DELETE_MEAL_NOT_ALLOWED);
            }
        }

        mealMapper.deleteMulti(ids);
        mealDishMapper.deleteByMealIds(ids);
    }

    @Override
    public MealVO getById(Long id) {
        Meal meal = mealMapper.getById(id);
        List<MealDish> mealDishes = mealDishMapper.getByMealId(id);

        MealVO mealVO = new MealVO();
        BeanUtils.copyProperties(meal, mealVO);
        mealVO.setMealDishes(mealDishes);

        return mealVO;
    }

    @Transactional
    @Override
    public void update(MealDTO mealDTO) {
        Meal meal = new Meal();
        BeanUtils.copyProperties(mealDTO, meal);

        mealMapper.update(meal);

        Long mealId = mealDTO.getId();
        mealDishMapper.deleteByMealIds(List.of(mealId));

        List<MealDish> mealDishes = mealDTO.getMealDishes();
        for (MealDish mealDish : mealDishes) {
            mealDish.setMealId(mealId);
        }

        mealDishMapper.insertMulti(mealDishes);
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        // If there are any disabled dish in this meal, it cannot be enabled
        if (Objects.equals(status, StatusConstant.ENABLE)) {
            Integer count = mealDishMapper.countByMealIdAndStatus(id, StatusConstant.DISABLE);
            if (count > 0) {
                throw new DeletionNotAllowedException(MessageConstant.MEAL_ENABLE_FAILED);
            }
        }

        Meal meal = Meal.builder()
                .id(id)
                .status(status)
                .build();

        mealMapper.update(meal);
    }

    @Override
    public List<Meal> getByMeal(Meal meal) {
        return mealMapper.getByMeal(meal);
    }

    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return mealMapper.getDishItemByMealId(id);
    }
}
