package com.mealmate.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {

    public static final Integer PENDING_PAYMENT = 1;
    public static final Integer PENDING_ORDER = 2;
    public static final Integer ORDER_RECEIVED = 3;
    public static final Integer DELIVERY_IN_PROGRESS = 4;
    public static final Integer COMPLETED = 5;
    public static final Integer CANCELLED = 6;
    public static final Integer REFUNDED = 7;

    public static final Integer UN_PAID = 0;
    public static final Integer PAID = 1;
    public static final Integer REFUND = 2;

    private static final long serialVersionUID = 1L;

    private Long id;

    private String serialNumber;

    private Integer status;

    private Long userId;

    private Long addressId;

    private LocalDateTime orderTime;

    private LocalDateTime checkoutTime;

    private Integer payMethod;

    private Integer payStatus;

    private BigDecimal price;

    private String note;

    private String userName;

    private String phone;

    private String address;

    private String name;

    private String cancelReason;

    private String rejectionReason;

    private LocalDateTime cancelTime;

    private LocalDateTime estimatedDeliveryTime;

    private Integer deliveryStatus;

    private LocalDateTime deliveryTime;

    private int packagingFee;

    private int tablewareNumber;

    private Integer tablewareStatus;

    private String paymentIntentId;
}
