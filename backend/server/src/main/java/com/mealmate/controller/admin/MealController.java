package com.mealmate.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mealmate.dto.MealDTO;
import com.mealmate.dto.MealPageQueryDTO;
import com.mealmate.result.PageResult;
import com.mealmate.result.Result;
import com.mealmate.service.MealService;
import com.mealmate.vo.MealVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController("AdminMealController")
@RequestMapping("/admin/meal")
@Tag(name = "Meal Controller")
@Slf4j
public class MealController {

    @Autowired
    private MealService mealService;

    @PostMapping
    @Operation(summary = "Add meal")
    @CacheEvict(value = "mealCache", key = "#mealDTO.categoryId")
    public Result<String> addMeal(@RequestBody MealDTO mealDTO) {
        log.info("addMeal: {}", mealDTO);
        mealService.addMeal(mealDTO);

        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "Page query meal")
    public Result<PageResult> getMealList(MealPageQueryDTO mealPageQueryDTO) {
        PageResult pageResult = mealService.getMealList(mealPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @Operation(summary = "Delete multiple meal")
    @CacheEvict(value = "mealCache", allEntries = true)
    public Result<String> deleteByMultiId(@RequestParam List<Long> ids) {
        mealService.deleteMulti(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get meal by id")
    public Result<MealVO> getById(@PathVariable Long id) {
        MealVO setmealVO = mealService.getById(id);
        return Result.success(setmealVO);
    }

    @PutMapping
    @Operation(summary = "update meal")
    @CacheEvict(value = "mealCache", allEntries = true)
    public Result<String> updateMeal(@RequestBody MealDTO mealDTO) {
        mealService.update(mealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @Operation(summary = "Update meal status")
    @CacheEvict(value = "mealCache", allEntries = true)
    public Result<String> updateStatus(@PathVariable("status") Integer status, Long id) {
        mealService.updateStatus(status, id);
        return Result.success();
    }
}
