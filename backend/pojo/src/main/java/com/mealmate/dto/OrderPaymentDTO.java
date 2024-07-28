package com.mealmate.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class OrderPaymentDTO implements Serializable {
    private String serialNumber;
    private Integer payMethod;

}
