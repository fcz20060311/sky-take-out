package com.sky.service;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import com.sky.service.impl.DishServiceImpl;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface DishService {
    void save(DishDTO dishDTO);

    PageResult pagequery(DishPageQueryDTO dishPageQueryDTO);

    void deleteids(List<Long> ids );

    DishVO getById(Long id);

    void update(DishDTO dishDTO);

    List<DishVO> listWithFlavor(Dish dish);

    void startOrStop(Integer status, Long id);
}
