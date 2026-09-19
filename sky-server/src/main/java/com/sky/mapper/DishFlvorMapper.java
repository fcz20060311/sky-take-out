package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlvorMapper {

    void insert(List<DishFlavor> flavors);

    int countByDishId(Long id);

    void deleteByids(List<Long> ids);

    List<DishFlavor> getByDishId(Long id);

    void deleteByid(Long id);
}
