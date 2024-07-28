package com.mealmate.mapper;

import com.github.pagehelper.Page;
import com.mealmate.annotation.AutoFill;
import com.mealmate.dto.EmployeePageQueryDTO;
import com.mealmate.entity.Employee;
import com.mealmate.enumeration.OperationType;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * get employee using username
     * @param userName
     * @return
     */
    @Select("select * from employee where user_name = #{userName}")
    Employee getByUsername(String userName);

    /**
     * insert employee
     * @param employee
     */
    @Insert("insert into employee(user_name, name, password, phone, status, create_time, update_time, create_user, update_user) values(#{userName}, #{name}, #{password}, #{phone}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Employee employee);

    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    @AutoFill(value = OperationType.UPDATE)
    void update(Employee employee);

    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);

    @Select("select shop_id from employee where id = #{employeeId}")
    Long getShopIdByEmployeeId(Long employeeId);
}
