package com.mealmate.controller.admin;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealmate.result.Result;
import com.mealmate.service.DashBoardService;
import com.mealmate.vo.BusinessDataVO;
import com.mealmate.vo.DishOverviewVO;
import com.mealmate.vo.MealOverviewVO;
import com.mealmate.vo.OrderOverviewVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController("AdminDashBoardController")
@RequestMapping("/admin/dashboard")
@Slf4j
@Tag(name = "DashBoard Controller")
public class DashBoardController {
    
    @Autowired
    private DashBoardService dashBoardService;

    @GetMapping("/businessData")
    @Operation(summary = "get business data") 
    public Result<BusinessDataVO> businessData(){
        LocalDate today = LocalDate.now();
        BusinessDataVO businessDataVO = dashBoardService.getBusinessData(today, today);
        return Result.success(businessDataVO);
    }

    @GetMapping("/overviewOrders")  
    @Operation(summary = "get order overview")
    public Result<OrderOverviewVO> orderOverview(){
        OrderOverviewVO orderOverviewVO = dashBoardService.getOrderOverview();
        return Result.success(orderOverviewVO);
    }

    @GetMapping("/overviewDishes")
    @Operation(summary = "get dish overview")
    public Result<DishOverviewVO> dishOverview(){
        DishOverviewVO dishOverviewVO = dashBoardService.getDishOverview();
        return Result.success(dishOverviewVO);
    }

    @GetMapping("/overviewMeals")
    @Operation(summary = "get meal overview")
    public Result<MealOverviewVO> mealOverview(){
        MealOverviewVO mealOverviewVO = dashBoardService.getMealOverview();
        return Result.success(mealOverviewVO);
    }
}
