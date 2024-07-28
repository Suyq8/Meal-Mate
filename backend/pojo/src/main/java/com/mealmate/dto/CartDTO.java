package com.mealmate.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CartDTO implements Serializable {

    private Long dishId;
    private Long setmealId;
    private String dishFlavor;

}
