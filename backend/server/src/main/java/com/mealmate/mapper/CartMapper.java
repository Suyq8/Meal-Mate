package com.mealmate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import com.mealmate.annotation.AutoFill;
import com.mealmate.entity.Cart;
import com.mealmate.enumeration.OperationType;

@Mapper
public interface CartMapper {
    
    /**
     * get cart by cart
     * @param cart
     * @return
     */
    List<Cart> getByCart(Cart cart);

    /**
     * update item quantity
     * @param cart
     */
    @Update("update cart set quantity = #{quantity} where id = #{id}")
    void updateItemQuantity(Cart cart);

    /**
     * add cart
     * @param cart
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Cart cart);

    /**
     * delete all items
     * @param userId
     */
    @Delete("delete from cart where user_id = #{userId}")
    void deleteByUserId(Long userId);

    /**
     * get items
     * @param id
     */
    @Delete("delete from cart where id = #{id}")
    void deleteById(Long id);

    /**
     * insert multi
     * @param carts
     */
    void insertMulti(List<Cart> carts);
}
