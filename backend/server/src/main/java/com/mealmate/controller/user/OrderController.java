package com.mealmate.controller.user;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealmate.constant.MessageConstant;
import com.mealmate.dto.OrderPaymentDTO;
import com.mealmate.dto.OrderSubmitDTO;
import com.mealmate.entity.Order;
import com.mealmate.result.PageResult;
import com.mealmate.result.Result;
import com.mealmate.service.OrderService;
import com.mealmate.utils.StripeUtil;
import com.mealmate.vo.OrderSubmitVO;
import com.mealmate.vo.OrderVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController("UserOrderController")
@RequestMapping("/user/order")
@Tag(name = "Order Controller (user)")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private StripeUtil stripeUtil;

    @PostMapping
    @Operation(summary = "submit order")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrderSubmitDTO orderSubmitDTO) {
        log.info("submit order: {}", orderSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(orderSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    @PutMapping("/payment")
    @Operation(summary = "pay for order")
    public void payment(@RequestBody OrderPaymentDTO orderPaymentDTO, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("payment: {}", orderPaymentDTO);

        Order order = orderService.getBySerialNumber(orderPaymentDTO.getSerialNumber());

        if (order == null || order.getPayStatus().equals(Order.PAID) || order.getPrice() == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        BigDecimal totalPrice = order.getPrice();
        String url = null;
        try {
            url = stripeUtil.checkOut(orderPaymentDTO.getSerialNumber(), totalPrice, request.getHeader("Origin"));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        log.info("url: {}", url);

        response.sendRedirect(url);
    }

    @GetMapping("/historyOrders")
    @Operation(summary = "get history orders")
    public Result<PageResult> getHistoryOrder(int page, int pageSize, Integer status) {
        PageResult pageResult = orderService.pageQuery(page, pageSize, status);
        return Result.success(pageResult);
    }

    @GetMapping("/orderDetail/{id}")
    @Operation(summary = "get order details")
    public Result<OrderVO> getDetails(@PathVariable("id") Long id) {
        OrderVO orderVO = orderService.getDetails(id);
        return Result.success(orderVO);
    }

    @PutMapping("/cancel/{id}")
    @Operation(summary = "cancel order")
    public Result<String> cancelOrder(@PathVariable("id") Long id) {
        Order orderDB = orderService.getById(id);

        if (orderDB == null) {
            return Result.error(MessageConstant.ORDER_NOT_FOUND);
        }

        // if order is paid and not delivering, then make a refund
        if (orderDB.getStatus().equals(Order.PENDING_ORDER) || orderDB.getStatus().equals(Order.ORDER_RECEIVED)) {
            try {
                stripeUtil.refund(orderDB.getPaymentIntentId());
            } catch (Exception e) {
                log.info("refund failed: {}", e.getMessage());
                return Result.error(e.getMessage());
            }
            orderDB.setPayStatus(Order.REFUNDED);
        } else {
            return Result.error(MessageConstant.ORDER_STATUS_ERROR);
        }

        Order order = Order.builder()
                .id(orderDB.getId())
                .payStatus(orderDB.getPayStatus())
                .status(Order.CANCELLED)
                .cancelReason("user cancel")
                .cancelTime(LocalDateTime.now())
                .build();
        orderService.update(order);

        return Result.success();
    }

    @PostMapping("/repetition/{id}")
    @Operation(summary = "make same order again")
    public Result<String> orderAgain(@PathVariable Long id) {
        orderService.orderAgain(id);
        return Result.success();
    }
}
