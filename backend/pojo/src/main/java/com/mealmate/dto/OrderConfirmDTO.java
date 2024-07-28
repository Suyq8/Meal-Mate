package com.mealmate.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class OrderConfirmDTO implements Serializable {
    private Long id;
    private Integer status;

}
