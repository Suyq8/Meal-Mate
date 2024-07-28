package com.mealmate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mealmate.entity.Address;

@Mapper
public interface AddressMapper {

    List<Address> getAllAddress(Address address);

    void insert(Address address);

    @Select("select * from address where id = #{id}")
    Address getById(Long id);

    void update(Address address);

    @Update("update address set is_default = #{isDefault} where user_id = #{userId}")
    void setDefaultByUserId(int isDefault, Long userId);

    @Delete("delete from address where id = #{id}")
    void deleteById(Long id);
}
