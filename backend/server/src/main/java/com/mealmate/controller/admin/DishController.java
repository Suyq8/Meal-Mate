package com.mealmate.controller.admin;

import java.util.List;
import java.util.Set;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mealmate.dto.DishDTO;
import com.mealmate.dto.DishPageQueryDTO;
import com.mealmate.entity.Dish;
import com.mealmate.result.PageResult;
import com.mealmate.result.Result;
import com.mealmate.service.DishService;
import com.mealmate.vo.DishVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * Dish Controller
 */
@RestController("AdminDishController")
@RequestMapping("/admin/dish")
@Tag(name = "Dish Controller")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private final String prefix = "DISH_";

    /**
     * Add a new dish
     *
     * @param dishDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "Add a new dish")
    public Result<String> addDish(@RequestBody DishDTO dishDTO) {
        log.info("addDish: {}", dishDTO);
        dishService.addDish(dishDTO);

        redisTemplate.delete(prefix + dishDTO.getCategoryId());

        return Result.success();
    }

    /**
     * Get dish list
     *
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "get dish list")
    public Result<PageResult> getDishList(@ParameterObject DishPageQueryDTO dishPageQueryDTO) {
        log.info("get dish list: {}", dishPageQueryDTO);
        PageResult res = dishService.getDishList(dishPageQueryDTO);

        return Result.success(res);
    }

    /**
     * delete dish by ids
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @Operation(summary = "delete dish by ids")
    public Result<String> deleteByMultiId(@RequestParam List<Long> ids) {
        log.info("delete dish by ids: {}", ids);
        dishService.deleteByMultiId(ids);

        for (Long id : ids) {
            redisTemplate.delete(prefix + id);
        }

        return Result.success();
    }

    /**
     * get dish by id
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "get dish by id")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("get dish by id: {}", id);
        DishVO dishVO = dishService.getById(id);

        return Result.success(dishVO);
    }

    @PutMapping
    @Operation(summary = "update dish")
    public Result<String> updateDish(@RequestBody DishDTO dishDTO) {
        log.info("update dish: {}", dishDTO);
        dishService.updateDish(dishDTO);

        // delete all dishes in redis
        // if category is changed, we should delete both dish_old_categoryId and dish_new_categoryId
        // here we just delete all dishes for simplicity
        Set<String> keys = redisTemplate.keys(prefix + "*");
        redisTemplate.delete(keys);

        return Result.success();
    }

    @PostMapping("/status/{status}")
    @Operation(summary = "update dish status")
    public Result<String> changeDishStatus(@PathVariable Integer status, Long id) {
        dishService.updateStatus(status, id);

        // delete all dishes in redis
        // or we have to get the category id of the dish and delete the corresponding key
        // here we just delete all dishes for simplicity
        Set<String> keys = redisTemplate.keys(prefix + "*");
        redisTemplate.delete(keys);

        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "get dish list by category id")
    public Result<List<Dish>> getDishByCategoryId(Long categoryId) {
        List<Dish> list = dishService.getDishByCategoryId(categoryId);
        return Result.success(list);
    }
}
