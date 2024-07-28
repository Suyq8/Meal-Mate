package com.mealmate.vo;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderStatisticsVO implements Serializable {
    private Integer toBeConfirmed;

    private Integer confirmed;

    private Integer deliveryInProgress;
}
