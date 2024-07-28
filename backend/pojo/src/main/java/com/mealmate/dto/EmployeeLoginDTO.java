package com.mealmate.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data model for employee login")
public class EmployeeLoginDTO implements Serializable {

    @Schema(description="userName")
    private String userName;

    @Schema(description="password")
    private String password;

}
