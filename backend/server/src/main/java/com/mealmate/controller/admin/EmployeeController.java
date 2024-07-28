package com.mealmate.controller.admin;

import com.mealmate.constant.JwtClaimsConstant;
import com.mealmate.dto.EmployeeLoginDTO;
import com.mealmate.entity.Employee;
import com.mealmate.properties.JwtProperties;
import com.mealmate.result.PageResult;
import com.mealmate.result.Result;
import com.mealmate.service.EmployeeService;
import com.mealmate.utils.CryptoUtil;
import com.mealmate.utils.JwtUtil;
import com.mealmate.vo.EmployeeLoginVO;
import com.mealmate.dto.EmployeeDTO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;

import com.mealmate.dto.EmployeePageQueryDTO;

/**
 * Employee Controller
 */
@RestController("AdminEmployeeController")
@RequestMapping("/admin/employee")
@Tag(name = "Employee Controller")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * log in
     *
     * @param employeeLoginDTO
     * @return
     * @throws Exception 
     */
    @PostMapping("/login")
    @Operation(summary = "log in")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) throws Exception {
        log.info("login: {}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        // After successful login, generate JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUserName())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * log out
     *
     * @return
     */
    @PostMapping("/logout")
    @Operation(summary = "log out")
    public Result<String> logout() {
        log.info("logout");
        return Result.success();
    }

    /**
     * add new employee
     *
     * @param employeeLoginDTO
     *
     * @return
     */
    @PostMapping
    @Operation(summary = "add employee")
    public Result<String> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("add employee: {}", employeeDTO);
        employeeService.addEmployee(employeeDTO);

        return Result.success();
    }

    /**
     * get employee list
     *
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "get employee list")
    public Result<PageResult> getEmployeeList(@ParameterObject EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("get employee list: {}", employeePageQueryDTO);
        PageResult res = employeeService.getEmployeeList(employeePageQueryDTO);

        return Result.success(res);
    }

    /**
     * update employee status
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @Operation(summary = "update employee status")
    public Result<String> updateEmployeeStatus(@PathVariable("status") Integer status, Long id) {
        log.info("update employee status: {}, {}", status, id);
        employeeService.updateEmployeeStatus(status, id);

        return Result.success();
    }

    /**
     * get employee using id
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "get employee using id")
    public Result<Employee> getEmployeeUsingId(@PathVariable("id") Long id) {
        log.info("get employee using id: {}", id);
        Employee employee = employeeService.getById(id);

        return Result.success(employee);
    }

    @PutMapping
    @Operation(summary = "update employee")
    public Result<String> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("update employee: {}", employeeDTO);
        employeeService.updateEmployee(employeeDTO);

        return Result.success();
    }
}
