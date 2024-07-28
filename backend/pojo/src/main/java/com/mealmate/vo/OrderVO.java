package com.mealmate.vo;

import java.util.List;

import com.mealmate.entity.OrderDetail;
import com.mealmate.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class OrderVO extends Order {

    private String orderDishes;

    private List<OrderDetail> orderDetailList;

}
