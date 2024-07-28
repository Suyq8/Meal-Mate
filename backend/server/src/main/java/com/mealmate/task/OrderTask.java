package com.mealmate.task;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mealmate.entity.Order;
import com.mealmate.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderService orderService;
    
    @Scheduled(cron = "0 * * * * ?") // every minute
    public void processTimeoutOrder () {
        log.info("process timeout order");

        List<Order> timeoutOrder = orderService.getTimeoutOrder(LocalDateTime.now().minusMinutes(15));

        if (timeoutOrder != null && !timeoutOrder.isEmpty()) {
            orderService.cancelMultiOrder(timeoutOrder, LocalDateTime.now(), "Timeout, cancel automatically");
        }
    }

    @Scheduled(cron = "0 0 1 * * ?") // every day at 1am
    public void processDeliveringOrder() {
        log.info("process delivering order");

        List<Order> deliveringOrder = orderService.getDeliveringOrder(LocalDateTime.now().toLocalDate().atStartOfDay());

        if (deliveringOrder != null && !deliveringOrder.isEmpty()) {
            orderService.setMultiOrderStatus(deliveringOrder, Order.COMPLETED);
        }
    }
}
