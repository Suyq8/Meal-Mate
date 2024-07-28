package com.mealmate.controller.admin;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealmate.constant.MessageConstant;
import com.mealmate.dto.OrderCancelDTO;
import com.mealmate.dto.OrderConfirmDTO;
import com.mealmate.dto.OrderPageQueryDTO;
import com.mealmate.dto.OrderRejectionDTO;
import com.mealmate.entity.Order;
import com.mealmate.exception.OrderBusinessException;
import com.mealmate.result.PageResult;
import com.mealmate.result.Result;
import com.mealmate.service.OrderService;
import com.mealmate.utils.StripeUtil;
import com.mealmate.vo.OrderStatisticsVO;
import com.mealmate.vo.OrderVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController("AdminOrderController")
@RequestMapping("/admin/order")
@Tag(name = "Order Controller (admin)")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private StripeUtil stripeUtil;

    @GetMapping("/conditionSearch")
    @Operation(summary = "get orders by condition")
    public Result<PageResult> getOrdersByCondition(@ParameterObject OrderPageQueryDTO orderPageQueryDTO) {
        log.info("orderPageQueryDTO: {}", orderPageQueryDTO);
        PageResult pageResult = orderService.conditionSearch(orderPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    @Operation(summary = "get order statistics")
    public Result<OrderStatisticsVO> getStatistics() {
        OrderStatisticsVO orderStatisticsVO = orderService.getStatistics();
        return Result.success(orderStatisticsVO);
    }

    @GetMapping("/details/{id}")
    @Operation(summary = "get order details by id")
    public Result<OrderVO> getDetails(@PathVariable("id") Long id) {
        OrderVO orderVO = orderService.getDetails(id);
        return Result.success(orderVO);
    }

    @PutMapping("/confirm")
    @Operation(summary = "confirm order")
    public Result<String> confirmOrder(@RequestBody OrderConfirmDTO orderConfirmDTO) {
        orderService.confirmOrder(orderConfirmDTO);
        return Result.success();
    }

    @PutMapping("/rejection")
    @Operation(summary = "reject order")
    public Result<String> rejectOrder(@RequestBody OrderRejectionDTO orderRejectionDTO) throws Exception {
        Order orderDB = orderService.getById(orderRejectionDTO.getId());

        if (orderDB == null || !Objects.equals(orderDB.getStatus(), Order.PENDING_ORDER)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        if (Objects.equals(orderDB.getPayStatus(), Order.PAID)) {
            stripeUtil.refund(orderDB.getPaymentIntentId());
        }

        Order order = Order.builder()
                .id(orderDB.getId())
                .status(Order.CANCELLED)
                .rejectionReason(orderRejectionDTO.getRejectionReason())
                .cancelTime(LocalDateTime.now())
                .payStatus(Order.REFUND)
                .build();

        orderService.update(order);
        return Result.success();
    }

    @PutMapping("/cancel")
    @Operation(summary = "cancel order")
    public Result<String> cancelOrder(@RequestBody OrderCancelDTO orderCancelDTO) throws Exception {
        Order orderDB = orderService.getById(orderCancelDTO.getId());

        if (orderDB == null || !Objects.equals(orderDB.getStatus(), Order.PENDING_ORDER)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        if (Objects.equals(orderDB.getPayStatus(), Order.PAID)) {
            stripeUtil.refund(orderDB.getPaymentIntentId());
        }

        Order order = Order.builder()
                .id(orderDB.getId())
                .status(Order.CANCELLED)
                .rejectionReason(orderCancelDTO.getCancelReason())
                .cancelTime(LocalDateTime.now())
                .payStatus(Order.REFUND)
                .build();

        orderService.update(order);
        return Result.success();
    }

    @PutMapping("/deliver/{id}")
    @Operation(summary = "delivery order")
    public Result<String> delivery(@PathVariable("id") Long id) {
        orderService.delivery(id);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    @Operation(summary = "complete order")
    public Result<String> completeOrder(@PathVariable("id") Long id) {
        orderService.complete(id);
        return Result.success();
    }

    @GetMapping("/reminder/{id}")
    @Operation(summary = "remind shop owever of order")
    public Result<String> reminder(@PathVariable("id") Long id) {
        orderService.reminder(id);
        return Result.success();
    }
}
