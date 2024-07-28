package com.mealmate.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;
import com.mealmate.dto.OrderPageQueryDTO;
import com.mealmate.entity.ItemSales;
import com.mealmate.entity.Order;

@Mapper
public interface OrderMapper {
    
    void insert(Order order);

    @Select("select * from `orders` where serial_number = #{serialNumber}")
    Order getBySerialNumber(String serialNumber);

    void update(Order order);

    @Select("select * from `orders` where status = #{status} and order_time < #{time}")
    List<Order> getByStatusAndLTTime(Integer status, LocalDateTime time);

    void updateMulti(List<Order> orders, Integer status, LocalDateTime cancelTime, String cancelReason);

    Page<Order> pageQuery(OrderPageQueryDTO orderPageQueryDTO);

    @Select("select * from `orders` where id = #{id}")
    Order getById(Long id);

    @Select("select count(id) from `orders` where status = #{status}")
    Integer countByStatus(Integer status);

    List<BigDecimal> getTurnoverList(LocalDate startDate, LocalDate endDate);

    List<Integer> getOrderList(LocalDate startDate, LocalDate endDate, Integer status);

    List<ItemSales> getTop10Sales(LocalDate startDate, LocalDate endDate);
}
