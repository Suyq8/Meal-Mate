package com.mealmate.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data format for employee login response")
public class EmployeeLoginVO implements Serializable {

    @Schema(description = "Primary key value")
    private Long id;

    @Schema(description = "Username")
    private String userName;

    @Schema(description = "Name")
    private String name;

    @Schema(description = "JWT token")
    private String token;

}
