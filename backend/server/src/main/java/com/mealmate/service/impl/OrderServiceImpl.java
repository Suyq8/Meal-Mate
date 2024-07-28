package com.mealmate.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mealmate.constant.MessageConstant;
import com.mealmate.context.BaseContext;
import com.mealmate.dto.OrderConfirmDTO;
import com.mealmate.dto.OrderPageQueryDTO;
import com.mealmate.dto.OrderSubmitDTO;
import com.mealmate.entity.Address;
import com.mealmate.entity.Cart;
import com.mealmate.entity.ItemSales;
import com.mealmate.entity.Order;
import com.mealmate.entity.OrderDetail;
import com.mealmate.exception.AddressBusinessException;
import com.mealmate.exception.CartBusinessException;
import com.mealmate.exception.OrderBusinessException;
import com.mealmate.mapper.AddressMapper;
import com.mealmate.mapper.CartMapper;
import com.mealmate.mapper.OrderDetailMapper;
import com.mealmate.mapper.OrderMapper;
import com.mealmate.result.PageResult;
import com.mealmate.service.OrderService;
import com.mealmate.vo.OrderStatisticsVO;
import com.mealmate.vo.OrderSubmitVO;
import com.mealmate.vo.OrderVO;
import com.mealmate.websocket.WebSocketServer;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public OrderSubmitVO submitOrder(OrderSubmitDTO orderSubmitDTO) {

        // check if address exists
        Address address = addressMapper.getById(orderSubmitDTO.getAddressId());
        if (address == null) {
            throw new AddressBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // check if cart is empty
        Cart cart = Cart.builder()
                .userId(BaseContext.getCurrentId())
                .build();
        List<Cart> carts = cartMapper.getByCart(cart);
        if (carts == null || carts.isEmpty()) {
            throw new CartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        BigDecimal price = new BigDecimal(0);
        for (Cart c : carts) {
            price = price.add(c.getPrice().multiply(BigDecimal.valueOf(c.getQuantity())));
        }

        StringBuilder sb = new StringBuilder();
        sb.append(address.getProvinceName() == null ? "" : address.getProvinceName())
                .append(address.getDistrictName() == null ? "" : address.getDistrictName())
                .append(address.getCityName() == null ? "" : address.getCityName())
                .append(address.getDetail() == null ? "" : address.getDetail());

        // insert order data to order table
        Order order = Order.builder()
                .userId(BaseContext.getCurrentId())
                .orderTime(LocalDateTime.now())
                .payStatus(Order.UN_PAID)
                .status(Order.PENDING_PAYMENT)
                .phone(address.getPhone())
                .name(address.getName())
                .serialNumber(String.valueOf(System.currentTimeMillis()))
                .price(price.add(BigDecimal.valueOf(orderSubmitDTO.getPacketagingFee())))
                .addressId(orderSubmitDTO.getAddressId())
                .address(sb.toString())
                .build();

        BeanUtils.copyProperties(orderSubmitDTO, order);
        orderMapper.insert(order);

        // insert item data to order_detail table
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (Cart c : carts) {
            OrderDetail orderDetail = OrderDetail.builder()
                    .orderId(order.getId())
                    .build();
            BeanUtils.copyProperties(c, orderDetail);
            orderDetails.add(orderDetail);
        }
        orderDetailMapper.insertMulti(orderDetails);

        // delete all items from cart table
        cartMapper.deleteByUserId(BaseContext.getCurrentId());

        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(order.getId())
                .serialNumber(order.getSerialNumber())
                .orderTime(LocalDateTime.now())
                .price(order.getPrice())
                .build();

        return orderSubmitVO;
    }

    @Override
    public Order getBySerialNumber(String serialNumber) {
        return orderMapper.getBySerialNumber(serialNumber);
    }

    @Override
    public void completeCheckoutSession(String serialNumber, Integer payMethod, String paymentIntentId) {
        Order orderDB = orderMapper.getBySerialNumber(serialNumber);

        Order order = Order.builder()
                .id(orderDB.getId())
                .status(Order.PENDING_ORDER)
                .payStatus(Order.PAID)
                .checkoutTime(LocalDateTime.now())
                .paymentIntentId(paymentIntentId)
                .build();

        orderMapper.update(order);

        // send message to admin that new order is created
        Map<String, Object> messageMap = new HashMap<>();

        messageMap.put("type", 1); // 1: new order, 2: reminder
        messageMap.put("orderId", orderDB.getId());
        messageMap.put("content", serialNumber);

        webSocketServer.sendToAllClient(JSON.toJSONString(messageMap));
    }

    @Override
    public List<Order> getTimeoutOrder(LocalDateTime time) {
        return orderMapper.getByStatusAndLTTime(Order.PENDING_PAYMENT, time);
    }

    @Override
    public List<Order> getDeliveringOrder(LocalDateTime time) {
        return orderMapper.getByStatusAndLTTime(Order.DELIVERY_IN_PROGRESS, time);
    }

    @Override
    public void cancelMultiOrder(List<Order> orders, LocalDateTime cancelTime, String reason) {
        orderMapper.updateMulti(orders, Order.CANCELLED, cancelTime, reason);
    }

    @Override
    public void setMultiOrderStatus(List<Order> orders, Integer status) {
        orderMapper.updateMulti(orders, status, null, null);
    }

    @Override
    public PageResult pageQuery(int pageNum, int pageSize, Integer status) {
        PageHelper.startPage(pageNum, pageSize);

        OrderPageQueryDTO orderPageQueryDTO = OrderPageQueryDTO.builder()
                .userId(BaseContext.getCurrentId())
                .status(status)
                .build();

        Page<Order> page = orderMapper.pageQuery(orderPageQueryDTO);

        List<OrderVO> orderVOList = new ArrayList<>();
        if (page != null && !page.isEmpty()) {
            for (Order order : page.getResult()) {
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(order.getId());

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(order, orderVO);
                orderVO.setOrderDetailList(orderDetails);
                orderVOList.add(orderVO);
            }
        }

        return new PageResult(orderVOList.size(), orderVOList);
    }

    @Override
    public OrderVO getDetails(Long id) {
        Order order = orderMapper.getById(id);
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(id);

        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderDetailList(orderDetails);

        return orderVO;
    }

    @Override
    public Order getById(Long id) {
        return orderMapper.getById(id);
    }

    @Override
    public void update(Order order) {
        orderMapper.update(order);
    }

    @Override
    public void orderAgain(Long id) {
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(id);

        if (orderDetails == null || orderDetails.isEmpty()) {
            throw new OrderBusinessException("order details not exist");
        }

        List<Cart> carts = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            Cart cart = Cart.builder()
                    .userId(BaseContext.getCurrentId())
                    .createTime(LocalDateTime.now())
                    .build();
            BeanUtils.copyProperties(orderDetail, cart, "id");

            carts.add(cart);
        }

        cartMapper.insertMulti(carts);
    }

    @Override
    public PageResult conditionSearch(OrderPageQueryDTO orderPageQueryDTO) {
        PageHelper.startPage(orderPageQueryDTO.getPage(), orderPageQueryDTO.getPageSize());

        Page<Order> page = orderMapper.pageQuery(orderPageQueryDTO);

        List<OrderVO> orderVOList = new ArrayList<>();
        if (page != null && !page.isEmpty()) {
            for (Order order : page.getResult()) {
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(order.getId());

                StringBuilder sb = new StringBuilder();
                for (OrderDetail orderDetail : orderDetails) {
                    sb.append(orderDetail.getName()).append("*").append(orderDetail.getQuantity()).append("; ");
                }

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(order, orderVO);
                orderVO.setOrderDishes(sb.toString());
                orderVOList.add(orderVO);
            }
        }

        return new PageResult(orderVOList.size(), orderVOList);
    }

    @Override
    public OrderStatisticsVO getStatistics() {
        OrderStatisticsVO orderStatisticsVO = OrderStatisticsVO.builder()
                .toBeConfirmed(orderMapper.countByStatus(Order.PENDING_ORDER))
                .confirmed(orderMapper.countByStatus(Order.ORDER_RECEIVED))
                .deliveryInProgress(orderMapper.countByStatus(Order.DELIVERY_IN_PROGRESS))
                .build();

        return orderStatisticsVO;
    }

    @Override
    public void confirmOrder(OrderConfirmDTO orderConfirmDTO) {
        Order order = Order.builder()
                .id(orderConfirmDTO.getId())
                .status(Order.ORDER_RECEIVED)
                .build();

        orderMapper.update(order);
    }

    @Override
    public void delivery(Long id) {
        Order orderDB = orderMapper.getById(id);

        // check if order exists and status is correct
        if (orderDB == null || !Objects.equals(orderDB.getStatus(), Order.ORDER_RECEIVED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Order order = Order.builder()
                .id(id)
                .status(Order.DELIVERY_IN_PROGRESS)
                .build();

        orderMapper.update(order);
    }

    @Override
    public void complete(Long id) {
        Order orderDB = orderMapper.getById(id);

        // check if order exists and status is correct
        if (orderDB == null || !Objects.equals(orderDB.getStatus(), Order.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Order order = Order.builder()
                .id(id)
                .status(Order.COMPLETED)
                .deliveryTime(LocalDateTime.now())
                .build();

        orderMapper.update(order);
    }

    @Override
    public void reminder(Long id) {
        Order orderDB = orderMapper.getById(id);

        // check if order exists and status is correct
        if (orderDB == null || ((!Objects.equals(orderDB.getStatus(), Order.DELIVERY_IN_PROGRESS)) && (!Objects.equals(orderDB.getStatus(), Order.ORDER_RECEIVED)))) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // send message to admin that new order is created
        Map<String, Object> messageMap = new HashMap<>();

        messageMap.put("type", 2); // 1: new order, 2: reminder
        messageMap.put("orderId", orderDB.getId());
        messageMap.put("content", orderDB.getSerialNumber());

        webSocketServer.sendToAllClient(JSON.toJSONString(messageMap));
    }

    @Override
    public List<BigDecimal> getTurnoverList(LocalDate startDate, LocalDate endDate) {
        return orderMapper.getTurnoverList(startDate, endDate);
    }

    @Override
    public List<Integer> getOrderCountList(LocalDate startDate, LocalDate endDate) {
        return orderMapper.getOrderList(startDate, endDate, null);
    }

    @Override
    public List<Integer> getValidOrderCountList(LocalDate startDate, LocalDate endDate) {
        return orderMapper.getOrderList(startDate, endDate, Order.COMPLETED);
    }

    @Override
    public List<ItemSales> getTop10Sales(LocalDate startDate, LocalDate endDate) {
        return orderMapper.getTop10Sales(startDate, endDate);
    }
}
