package com.mealmate.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EmployeeDTO implements Serializable {

    private Long id;

    private String userName;

    private String name;

    private String phone;

}