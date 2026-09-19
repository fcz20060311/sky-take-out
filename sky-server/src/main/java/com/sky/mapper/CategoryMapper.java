package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    Page<Category> pagequery(CategoryPageQueryDTO categoryPageQueryDTO);

    @AutoFill(value= OperationType.UPDATE)
    void update(Category category);

    @AutoFill(value=OperationType.INSERT)
    void insert(Category category);

    List<Category> listByType(Integer type);

    Category getById(Long categoryId);
}
