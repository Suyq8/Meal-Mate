package com.mealmate.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealmate.result.Result;
import com.mealmate.service.DishService;
import com.mealmate.vo.DishVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController("UserDishController")
@RequestMapping("/user/dish")
@Tag(name = "Dish Controller (user)")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private final String prefix = "DISH_";

    /**
     * get dish by category id
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "get dish by category id")
    public Result<List<DishVO>> getByCategoryId(Long categoryId) {
        // check if the data is in redis
        String key = prefix + categoryId;
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if(list!=null && !list.isEmpty()){
            return Result.success(list);
        }

        list = dishService.getDishVO(categoryId);

        // save to redis
        redisTemplate.opsForValue().set(key, list);

        return Result.success(list);
    }
}
