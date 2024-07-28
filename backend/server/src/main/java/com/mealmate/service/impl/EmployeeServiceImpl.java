package com.mealmate.service.impl;

import com.mealmate.constant.MessageConstant;
import com.mealmate.constant.StatusConstant;
import com.mealmate.dto.EmployeeDTO;
import com.mealmate.dto.EmployeeLoginDTO;
import com.mealmate.dto.EmployeePageQueryDTO;
import com.mealmate.entity.Employee;
import com.mealmate.exception.AccountLockedException;
import com.mealmate.exception.AccountNotFoundException;
import com.mealmate.exception.PasswordErrorException;
import com.mealmate.exception.UserExistException;
import com.mealmate.mapper.EmployeeMapper;
import com.mealmate.result.PageResult;
import com.mealmate.service.EmployeeService;
import com.mealmate.utils.CryptoUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mealmate.constant.PasswordConstant;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private CryptoUtil cryptoUtil;

    /**
     * employee log in
     *
     * @param employeeLoginDTO
     * @return
     * @throws Exception 
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) throws Exception {
        //String username = employeeLoginDTO.getUsername();
        //String password = employeeLoginDTO.getPassword();
        String username = cryptoUtil.decryptAES(employeeLoginDTO.getUserName());
        String password = cryptoUtil.decryptAES(employeeLoginDTO.getPassword());

        Employee employee = employeeMapper.getByUsername(username);

        // handle exception scenarios, e.g. username not found, incorrect password, locked account
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // compare password after md5 encryption
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (StatusConstant.DISABLE.equals(employee.getStatus())) {
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        return employee;
    }

    /**
     * add new employee
     *
     * @param employeeLoginDTO
     *
     * @return
     */
    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        Employee e = employeeMapper.getByUsername(employee.getUserName());
        if (e != null) { // user name exists
            throw new UserExistException(String.format(MessageConstant.USER_EXIST, employee.getUserName()));
        }

        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        employeeMapper.insert(employee);
    }

    /**
     * page query
     *
     * @param employeePageQueryDTO
     * @return PageResult
     */
    @Override
    public PageResult getEmployeeList(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void updateEmployeeStatus(Integer status, Long id) {
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee);
    }

    @Override
    public Employee getById(Long id) {
        Employee employee = employeeMapper.getById(id);
        employee.setPassword(null);

        return employee;
    }

    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        employeeMapper.update(employee);
    }

    @Override
    public Long getShopIdByEmployeeId(Long employeeId){
        return employeeMapper.getShopIdByEmployeeId(employeeId);
    }
}
