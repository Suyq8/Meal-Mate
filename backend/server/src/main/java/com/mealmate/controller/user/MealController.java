package com.mealmate.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealmate.constant.StatusConstant;
import com.mealmate.entity.Meal;
import com.mealmate.result.Result;
import com.mealmate.service.MealService;
import com.mealmate.vo.DishItemVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController("UserMealController")
@RequestMapping("/user/setmeal")
@Tag(name = "Meal Controller (user)")
public class MealController {

    @Autowired
    private MealService mealService;

    /**
     * get meal list
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "get meal list")
    @Cacheable(value = "mealCache", key = "#categoryId")
    public Result<List<Meal>> getByCategoryId(Long categoryId) {
        Meal meal = Meal.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();

        List<Meal> list = mealService.getByMeal(meal);
        return Result.success(list);
    }

    /**
     * get dish list by meal id
     *
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    @Operation(summary = "get dish list by meal id")
    public Result<List<DishItemVO>> getDish(@PathVariable("id") Long id) {
        List<DishItemVO> list = mealService.getDishItemById(id);
        return Result.success(list);
    }
}
