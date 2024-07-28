package com.mealmate.service;

import java.util.List;

import com.mealmate.dto.CartDTO;
import com.mealmate.entity.Cart;

public interface CartService {
    /**
     * add cart
     * @param cartDTO
     */
    void addCart(CartDTO cartDTO);

    /**
     * get items
     * @return
     */
    List<Cart> getItems();

    /**
     * delete all items
     * @return
     */
    void deleteAllItem();

    /**
     * delete one item
     * @param cartDTO
     */
    void deleteOneItem(CartDTO cartDTO);
}
