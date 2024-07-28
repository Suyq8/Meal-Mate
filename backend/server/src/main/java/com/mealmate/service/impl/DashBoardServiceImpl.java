package com.mealmate.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mealmate.constant.StatusConstant;
import com.mealmate.entity.Order;
import com.mealmate.mapper.DishMapper;
import com.mealmate.mapper.MealMapper;
import com.mealmate.mapper.OrderMapper;
import com.mealmate.service.DashBoardService;
import com.mealmate.service.OrderService;
import com.mealmate.service.UserService;
import com.mealmate.vo.BusinessDataVO;
import com.mealmate.vo.DishOverviewVO;
import com.mealmate.vo.MealOverviewVO;
import com.mealmate.vo.OrderOverviewVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DashBoardServiceImpl implements DashBoardService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private MealMapper mealMapper;

    @Override
    public BusinessDataVO getBusinessData(LocalDate startDate, LocalDate endDate) {
        Integer orderCount = orderService.getOrderCountList(startDate, endDate).get(0);
        Integer validOrderCount = orderService.getValidOrderCountList(startDate, endDate).get(0);
        BigDecimal turnover = orderService.getTurnoverList(startDate, endDate).get(0);
        List<Integer> userCountList = userService.getUserCountList(startDate.minusDays(1), endDate);

        BusinessDataVO businessDataVO = BusinessDataVO.builder()
                .turnover(turnover.doubleValue())
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCount == 0 ? 0 : (double) validOrderCount / orderCount)
                .unitPrice(validOrderCount == 0 ? 0 : turnover.doubleValue() / validOrderCount)
                .newUsers(userCountList.get(1) - userCountList.get(0))
                .build();

        return businessDataVO;
    }

    @Override
    public OrderOverviewVO getOrderOverview() {
        LocalDate date = LocalDate.now();
        OrderOverviewVO orderOverviewVO = OrderOverviewVO.builder()
                .waitingOrders(orderMapper.getOrderList(date, date, Order.PENDING_ORDER).get(0))
                .deliveringOrders(orderMapper.getOrderList(date, date, Order.ORDER_RECEIVED).get(0))
                .completedOrders(orderMapper.getOrderList(date, date, Order.COMPLETED).get(0))
                .cancelledOrders(orderMapper.getOrderList(date, date, Order.CANCELLED).get(0))
                .allOrders(orderMapper.getOrderList(date, date, null).get(0))
                .build();

        return orderOverviewVO;
    }

    @Override
    public DishOverviewVO getDishOverview() {
        DishOverviewVO dishOverviewVO = DishOverviewVO.builder()
                .selling(dishMapper.countByStatus(StatusConstant.ENABLE))
                .stopSelling(dishMapper.countByStatus(StatusConstant.DISABLE))
                .build();

        return dishOverviewVO;
    }

    @Override
    public MealOverviewVO getMealOverview() {
        MealOverviewVO mealOverviewVO = MealOverviewVO.builder()
                .selling(mealMapper.countByStatus(StatusConstant.ENABLE))
                .stopSelling(mealMapper.countByStatus(StatusConstant.DISABLE))
                .build();

        return mealOverviewVO;
    } 
}
