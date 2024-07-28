package com.mealmate.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class OrderCancelDTO implements Serializable {

    private Long id;
    private String cancelReason;

}
