package com.mealmate.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mealmate.context.BaseContext;
import com.mealmate.dto.CartDTO;
import com.mealmate.entity.Cart;
import com.mealmate.entity.Dish;
import com.mealmate.entity.Meal;
import com.mealmate.mapper.CartMapper;
import com.mealmate.mapper.DishMapper;
import com.mealmate.mapper.MealMapper;
import com.mealmate.service.CartService;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartMapper cartMapper;
    @Autowired
    MealMapper mealMapper;
    @Autowired
    DishMapper dishMapper;

    @Override
    public void addCart(CartDTO cartDTO) {
        Cart cart = Cart.builder()
                .userId(BaseContext.getCurrentId())
                .dishId(cartDTO.getDishId())
                .mealId(cartDTO.getSetmealId())
                .flavor(cartDTO.getDishFlavor())
                .build();

        List<Cart> carts = cartMapper.getByCart(cart);

        // the item already in cart
        if (carts != null && !carts.isEmpty()) {
            cart = carts.get(0);
            cart.setQuantity(cart.getQuantity() + 1);
            cartMapper.updateItemQuantity(cart);
        } else {
            // new item is meal
            if (cartDTO.getSetmealId() != null) {
                Meal meal = mealMapper.getById(cartDTO.getSetmealId());
                cart.setImage(meal.getImage());
                cart.setPrice(meal.getPrice());
                cart.setName(meal.getName());
            } else { // dish
                Dish dish = dishMapper.getById(cartDTO.getDishId());
                cart.setImage(dish.getImage());
                cart.setPrice(dish.getPrice());
                cart.setName(dish.getName());
            }
            cart.setCreateTime(LocalDateTime.now());
            cart.setQuantity(1);
            cartMapper.insert(cart);
        }
    }

    @Override
    public List<Cart> getItems() {
        Cart cart = Cart.builder()
                .userId(BaseContext.getCurrentId())
                .build();
        return cartMapper.getByCart(cart);
    }

    @Override
    public void deleteAllItem() {
        cartMapper.deleteByUserId(BaseContext.getCurrentId());
    }

    @Override
    public void deleteOneItem(CartDTO cartDTO) {
        Cart cart = Cart.builder()
                .userId(BaseContext.getCurrentId())
                .dishId(cartDTO.getDishId())
                .mealId(cartDTO.getSetmealId())
                .flavor(cartDTO.getDishFlavor())
                .build();

        List<Cart> carts = cartMapper.getByCart(cart);

        // the item already in cart
        if (carts != null && !carts.isEmpty()) {
            cart = carts.get(0);
            if (cart.getQuantity() > 1) {
                cart.setQuantity(cart.getQuantity() - 1);
                cartMapper.updateItemQuantity(cart);
            } else {
                cartMapper.deleteById(cart.getId());
            }
        }
    }
}
