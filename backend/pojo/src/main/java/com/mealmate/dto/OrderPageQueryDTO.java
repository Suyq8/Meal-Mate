package com.mealmate.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageQueryDTO implements Serializable {

    private int page;

    private int pageSize;

    private String serialNumber;

    private String phone;

    private Integer status;

    private LocalDate beginTime;

    private LocalDate endTime;

    private Long userId;

}
