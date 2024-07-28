package com.mealmate.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    
    List<Integer> getUserCountList(LocalDate startDate, LocalDate endDate);
}
