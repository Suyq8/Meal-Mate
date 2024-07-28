package com.mealmate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;
import com.mealmate.annotation.AutoFill;
import com.mealmate.dto.DishPageQueryDTO;
import com.mealmate.entity.Dish;
import com.mealmate.enumeration.OperationType;
import com.mealmate.vo.DishVO;

@Mapper
public interface DishMapper {
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * delete dish by multiple id
     * @param ids
     */
    void deleteByMultiId(List<Long> dishIds);

    /**
     * get dish by id
     * @param id
     * @return
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    List<Dish> getByDish(Dish dish);

    @Select("select count(id) from dish where status = #{status}")
    Integer countByStatus(Integer status);
}
