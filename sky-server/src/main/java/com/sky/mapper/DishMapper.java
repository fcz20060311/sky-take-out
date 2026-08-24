package com.sky.mapper;

import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper {

    int countById(Long id);
}
