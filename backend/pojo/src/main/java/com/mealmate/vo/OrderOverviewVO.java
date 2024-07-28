package com.mealmate.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderOverviewVO implements Serializable {

    private Integer waitingOrders;

    private Integer deliveringOrders;

    private Integer completedOrders;

    private Integer cancelledOrders;

    private Integer allOrders;
}
