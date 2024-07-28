package com.mealmate.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrderSubmitDTO implements Serializable {
    private Long addressId;
    private int payMethod;
    private String note;
    private LocalDateTime estimatedDeliveryTime;
    private Integer deliveryStatus;
    private Integer tablewareNumber;
    private Integer tablewareStatus;
    private Integer packetagingFee;
}
