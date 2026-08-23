package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

public interface CategoryService {
    PageResult pagequerry(CategoryPageQueryDTO categoryPageQueryDTO);

    void update(CategoryDTO categorydto);

    void status(Integer status, Long id);
}
