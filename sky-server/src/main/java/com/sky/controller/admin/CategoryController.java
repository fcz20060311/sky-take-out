package com.sky.controller.admin;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
