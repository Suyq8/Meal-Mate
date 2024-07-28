package com.mealmate.mapper;

import com.github.pagehelper.Page;
import com.mealmate.annotation.AutoFill;
import com.mealmate.dto.CategoryPageQueryDTO;
import com.mealmate.entity.Category;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import com.mealmate.enumeration.OperationType;

@Mapper
public interface CategoryMapper {

    /**
     * insert category
     * @param category
     */
    @Insert("insert into category(type, name, ranking, status, create_time, update_time, create_user, update_user)" +
            " VALUES" +
            " (#{type}, #{name}, #{ranking}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Category category);

    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * delete category by id
     * @param id
     */
    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);

    /**
     * update category
     * @param category
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    List<Category> getByType(Integer type);
}
