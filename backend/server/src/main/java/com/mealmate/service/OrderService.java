package com.mealmate.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.mealmate.dto.OrderConfirmDTO;
import com.mealmate.dto.OrderPageQueryDTO;
import com.mealmate.dto.OrderSubmitDTO;
import com.mealmate.entity.ItemSales;
import com.mealmate.entity.Order;
import com.mealmate.result.PageResult;
import com.mealmate.vo.OrderStatisticsVO;
import com.mealmate.vo.OrderSubmitVO;
import com.mealmate.vo.OrderVO;

public interface OrderService {

    /**
     * Submit order
     *
     * @param orderSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrderSubmitDTO orderSubmitDTO);

    /**
     * get by serial number
     *
     * @param serialNumber
     * @return
     */
    Order getBySerialNumber(String serialNumber);

    /**
     * Complete checkout session
     *
     * @param serialNumber
     * @param payMethod
     * @param paymentIntentId
     */
    void completeCheckoutSession(String serialNumber, Integer payMethod, String paymentIntentId);

    /**
     * get timeout order
     *
     * @param time
     * @return
     */
    List<Order> getTimeoutOrder(LocalDateTime time);

    /**
     * get delivering order
     *
     * @param time
     * @return
     */
    List<Order> getDeliveringOrder(LocalDateTime time);

    /**
     * cancel multi order
     *
     * @param orders
     * @param time
     * @param reason
     */
    void cancelMultiOrder(List<Order> orders, LocalDateTime cancelTime, String reason);

    /**
     * set multi order status
     *
     * @param orders
     * @param status
     */
    void setMultiOrderStatus(List<Order> orders, Integer status);

    /**
     * page query
     *
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult pageQuery(int pageNum, int pageSize, Integer status);

    /**
     * get details
     *
     * @param id
     * @return
     */
    OrderVO getDetails(Long id);

    /**
     * get by id
     *
     * @param id
     */
    Order getById(Long id);

    /**
     * update
     *
     * @param order
     */
    void update(Order order);

    /**
     * order again
     *
     * @param id
     */
    void orderAgain(Long id);

    /**
     * condition search
     *
     * @param orderPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrderPageQueryDTO orderPageQueryDTO);

    /**
     * get statistics
     *
     * @return
     */
    OrderStatisticsVO getStatistics();

    /**
     * confirm order
     *
     * @param orderConfirmDTO
     */
    void confirmOrder(OrderConfirmDTO orderConfirmDTO);

    /**
     * deliver order
     *
     * @param order
     */
    void delivery(Long id);

    /**
     * complete order
     *
     * @param id
     */
    void complete(Long id);

    /**
     * remind order
     *
     * @param id
     */
    void reminder(Long id);

    /**
     * get turnover list
     *
     * @param startDate
     * @param endDate
     * @return
     */
    List<BigDecimal> getTurnoverList(LocalDate startDate, LocalDate endDate);

    /**
     * get order count list
     * @param startDate
     * @param endDate
     * @return
     */
    List<Integer> getOrderCountList(LocalDate startDate, LocalDate endDate);

    /**
     * get valid order count list
     * @param startDate
     * @param endDate
     * @return
     */
    List<Integer> getValidOrderCountList(LocalDate startDate, LocalDate endDate);

    /**
     * get top 10 sales
     * @param startDate
     * @param endDate
     * @return
     */
    List<ItemSales> getTop10Sales(LocalDate startDate, LocalDate endDate);
}
