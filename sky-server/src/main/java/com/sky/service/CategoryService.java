package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    PageResult pagequerry(CategoryPageQueryDTO categoryPageQueryDTO);

    void update(CategoryDTO categorydto);

    void status(Integer status, Long id);

    void add(CategoryDTO categoryDTO);

    List<Category> listByType(Integer type);
}
