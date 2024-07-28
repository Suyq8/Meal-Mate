package com.mealmate.service;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    
    /**
     * get user count list
     * @param startDate
     * @param endDate
     * @return
     */
    List<Integer> getUserCountList(LocalDate startDate, LocalDate endDate);
}
