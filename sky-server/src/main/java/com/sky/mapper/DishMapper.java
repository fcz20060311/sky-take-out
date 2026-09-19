package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishMapper {

    int countById(Long id);

    @AutoFill(value= OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> pagequery(DishPageQueryDTO dishPageQueryDTO);

    Dish getById(Long id);

    void deleteByids(List<Long> ids);

    @AutoFill(value= OperationType.UPDATE)
    void update(Dish dish);


    List<Dish> list(Dish dish);

    /**
     * 根据套餐id查询套餐内的菜品
     * @param setmealId
     * @return
     */
    List<Dish> getBySetmealId(Long setmealId);
}
