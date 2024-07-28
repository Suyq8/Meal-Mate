package com.mealmate.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mealmate.mapper.UserMapper;
import com.mealmate.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Integer> getUserCountList(LocalDate startDate, LocalDate endDate) {
        return userMapper.getUserCountList(startDate, endDate);
    }
}
