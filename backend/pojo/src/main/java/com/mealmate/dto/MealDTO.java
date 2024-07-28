package com.mealmate.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mealmate.entity.MealDish;

import lombok.Data;

@Data
public class MealDTO implements Serializable {

    private Long id;

    private Long categoryId;

    private String name;

    private BigDecimal price;

    private Integer status;

    private String description;

    private String image;

    private List<MealDish> mealDishes = new ArrayList<>();

}
