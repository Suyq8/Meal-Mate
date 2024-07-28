package com.mealmate.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealmate.context.BaseContext;
import com.mealmate.result.Result;
import com.mealmate.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController("UserShopController")
@RequestMapping("/user/shop")
@Tag(name = "Shop Controller (user)")
@Slf4j
public class ShopController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/status")
    @Operation(summary = "Get shop status")
    public Result<Integer> getStatus() {
        // todo: get status by shopid
        Long userId = BaseContext.getCurrentId();
        Long shopId = employeeService.getShopIdByEmployeeId(userId);
        Integer status = (Integer) redisTemplate.opsForHash().get("SHOP_STATUS", shopId);
        return Result.success(status);
    }
}
