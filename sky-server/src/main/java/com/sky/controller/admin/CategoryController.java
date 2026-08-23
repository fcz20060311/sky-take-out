package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags="分类相关接口")
@RestController
@Slf4j
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("分页查询分类")
    @GetMapping("/page")
    public Result<PageResult> pagequery(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询分类:{}", categoryPageQueryDTO);
        PageResult pageResult=categoryService.pagequerry(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    @ApiOperation("修改分类")
    @PutMapping
    public Result update(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类:{}",categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    @ApiOperation("启用禁用")
    @PostMapping("/status/{status}")
    public Result status(@PathVariable Integer status,Long id){
        log.info("启用禁用分类:{}",id);
        categoryService.status(status,id);
        return Result.success();
    }
}
