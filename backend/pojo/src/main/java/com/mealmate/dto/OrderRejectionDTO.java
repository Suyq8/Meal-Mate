package com.mealmate.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class OrderRejectionDTO implements Serializable {

    private Long id;

    private String rejectionReason;

}
