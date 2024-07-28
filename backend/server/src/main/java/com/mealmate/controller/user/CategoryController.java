package com.mealmate.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealmate.entity.Category;
import com.mealmate.result.Result;
import com.mealmate.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController("UserCategoryController")
@RequestMapping("/user/category")
@Tag(name = "Category Controller (user)")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * get category list
     * @param type
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "get category list")
    public Result<List<Category>> getCategoryList(Integer type) {
        List<Category> list = categoryService.getCategoryUsingType(type);
        return Result.success(list);
    }
}
