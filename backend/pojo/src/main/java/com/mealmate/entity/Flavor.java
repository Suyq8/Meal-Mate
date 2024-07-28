package com.mealmate.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Flavor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long dishId;

    private String name;

    private String value;

}
