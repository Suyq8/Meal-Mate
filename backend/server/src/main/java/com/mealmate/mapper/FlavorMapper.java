package com.mealmate.mapper;

import com.mealmate.entity.Flavor;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface FlavorMapper {

    /**
     * Insert multiple flavors
     * @param flavors
     */
    void insertMulti(List<Flavor> flavors);

    /**
     * Get flavors by dish id
     * @param dishId
     * @return
     */
    @Select("select * from flavor where dish_id = #{dishId}")
    List<Flavor> getByDishId(Long dishId);

    /**
     * Delete flavors by dish id
     * @param dishId
     */
    @Delete("delete from flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    /**
     * Delete flavors by multiple dish id
     * @param dishIds
     */
    void deleteByMultiId(List<Long> dishIds);
}
