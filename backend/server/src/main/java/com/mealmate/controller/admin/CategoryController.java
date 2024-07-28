package com.mealmate.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealmate.dto.CategoryDTO;
import com.mealmate.dto.CategoryPageQueryDTO;
import com.mealmate.entity.Category;
import com.mealmate.result.PageResult;
import com.mealmate.result.Result;
import com.mealmate.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController("AdminCategoryController")
@RequestMapping("/admin/category")
@Tag(name = "Category Controller")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * add new category
     *
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "add new category")
    public Result<String> addCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("add new category: {}", categoryDTO);
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    /**
     * get category list
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "get category list")
    public Result<PageResult> getCategoryList(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("get category list: {}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.getCategoryList(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * delete category using id
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @Operation(summary = "delete category using id")
    public Result<String> deleteUsingId(Long id) {
        log.info("delete category: {}", id);
        categoryService.deleteUsingId(id);
        return Result.success();
    }

    /**
     * update category
     *
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @Operation(summary = "update category")
    public Result<String> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }

    /**
     * change category status
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @Operation(summary = "change category status")
    public Result<String> changeCategoryStatus(@PathVariable("status") Integer status, Long id) {
        categoryService.changeCategoryStatus(status, id);
        return Result.success();
    }

    /**
     * get category using type
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "get category using type")
    public Result<List<Category>> getCategoryUsingType(Integer type) {
        List<Category> list = categoryService.getCategoryUsingType(type);
        return Result.success(list);
    }
}
