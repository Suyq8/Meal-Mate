package com.mealmate.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CategoryPageQueryDTO implements Serializable {

    private int page;

    private int pageSize;

    private String name;

    private Integer type;

}
