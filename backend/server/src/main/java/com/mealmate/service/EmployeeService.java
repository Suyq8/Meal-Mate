package com.mealmate.service;

import com.mealmate.dto.EmployeeDTO;
import com.mealmate.dto.EmployeeLoginDTO;
import com.mealmate.dto.EmployeePageQueryDTO;
import com.mealmate.entity.Employee;
import com.mealmate.result.PageResult;

public interface EmployeeService {

    /**
     * employee log in
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO) throws Exception;

    /**
     * add new employee
     * @param employeeLoginDTO
     *
     * @return
     */
    void addEmployee(EmployeeDTO employeeDTO);

    /**
     * page query
     * @param employeePageQueryDTO
     * @return PageResult
     */
    PageResult getEmployeeList(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * update employee status
     * @param status
     * @param id
     */
    void updateEmployeeStatus(Integer status, Long id);

    /**
     * get employee using id
     * @param id
     * @return
     */
    Employee getById(Long id);

    /**
     * update employee
     * @param employeeDTO
     */
    void updateEmployee(EmployeeDTO employeeDTO);

    /**
     * get shop id by employee id
     * @param employeeId
     * @return
     */
    Long getShopIdByEmployeeId(Long employeeId);
}
