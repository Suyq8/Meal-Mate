package com.mealmate.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealmate.dto.CartDTO;
import com.mealmate.entity.Cart;
import com.mealmate.result.Result;
import com.mealmate.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController("UserCartController")
@RequestMapping("/user/shoppingCart")
@Tag(name = "Cart Controller (user)")
@Slf4j
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * add cart
     *
     * @param cartDTO
     * @return
     */
    @PostMapping("/add")
    @Operation(summary = "add cart")
    public Result<String> addCart(@RequestBody CartDTO cartDTO) {
        log.info("add cart: {}", cartDTO);
        cartService.addCart(cartDTO);

        return Result.success();
    }

    /**
     * get items
     *
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "get items")
    public Result<List<Cart>> getItems() {
        return Result.success(cartService.getItems());
    }

    /**
     * delete all items
     * @return
     */
    @DeleteMapping("/clean")
    @Operation(summary = "delete all items")
    public Result<String> deleteAllItem() {
        cartService.deleteAllItem();
        return Result.success();
    }

    /**
     * delete one item
     * @param cartDTO
     * @return
     */
    @PostMapping("/sub")
    @Operation(summary = "delete one item")
    public Result<String> deleteOneItem(@RequestBody CartDTO cartDTO) {
        cartService.deleteOneItem(cartDTO);
        return Result.success();
    }
}
