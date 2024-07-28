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
public class DishOverviewVO implements Serializable {

    private Integer selling;

    private Integer stopSelling;
}
