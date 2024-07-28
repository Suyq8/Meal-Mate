package com.mealmate.service;

import java.time.LocalDate;

import com.mealmate.vo.BusinessDataVO;
import com.mealmate.vo.DishOverviewVO;
import com.mealmate.vo.MealOverviewVO;
import com.mealmate.vo.OrderOverviewVO;

public interface DashBoardService {
    
    BusinessDataVO getBusinessData(LocalDate startDate, LocalDate endDate);

    OrderOverviewVO getOrderOverview();

    DishOverviewVO getDishOverview();

    MealOverviewVO getMealOverview();
}
