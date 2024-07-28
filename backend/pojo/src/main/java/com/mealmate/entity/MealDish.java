package com.mealmate.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealDish implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long mealId;

    private Long dishId;

    private String name;

    private BigDecimal price;

    private Integer quantity;
}
