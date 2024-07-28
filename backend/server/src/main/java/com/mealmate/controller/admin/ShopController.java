package com.mealmate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealmate.context.BaseContext;
import com.mealmate.result.Result;
import com.mealmate.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController("AdminShopController")
@RequestMapping("/admin/shop")
@Tag(name = "Shop Controller (server)")
@Slf4j
public class ShopController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private EmployeeService employeeService;

    @PutMapping("/{status}")
    @Operation(summary = "Update shop status")
    public Result<String> updateStatus(@PathVariable("status") Integer status) {
        // todo: update if have permission
        log.info("updateStatus: {}", status);
        Long userId = BaseContext.getCurrentId();
        Long shopId = employeeService.getShopIdByEmployeeId(userId);
        redisTemplate.opsForHash().put("SHOP_STATUS", shopId, status);

        return Result.success();
    }

    @GetMapping("/status")
    @Operation(summary = "Get shop status")
    public Result<Integer> getStatus() {
        // todo: shopid
        Long userId = BaseContext.getCurrentId();
        Long shopId = employeeService.getShopIdByEmployeeId(userId);
        Integer status = (Integer) redisTemplate.opsForHash().get("SHOP_STATUS", shopId);
        return Result.success(status);
    }
}
