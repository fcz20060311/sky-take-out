package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    DishMapper dishMapper;

    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {

        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    public SetmealVO getById(Long id) {
        //查询套餐基本信息
        Setmeal setmeal=setmealMapper.getById(id);
        if(setmeal==null){
            return null;
        }

        //查询套餐关联的菜品
        List<SetmealDish> setmealDishes=setmealDishMapper.getBySetmealId(id);

        //查询分类名称
        Category category=categoryMapper.getById(setmeal.getCategoryId());
        String categoryName=category==null?null:category.getName();

        //组装返回的VO
        SetmealVO setmealVO=new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setCategoryName(categoryName);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;

    }

    @Transactional
    public void save(SetmealDTO setmealDTO){
        //保存套餐基本信息
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.insert(setmeal);

        //获取套餐主键(id)
        Long setmealId=setmeal.getId();

        //保存套餐和菜品的关联关系
        List<SetmealDish> setmealDishes=setmealDTO.getSetmealDishes();
        if(setmealDishes !=null && setmealDishes.size()>0){
            setmealDishes.forEach(setmealDish->{
                setmealDish.setSetmealId(setmealId);
            });
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    @Transactional
    public void update(SetmealDTO setmealDTO){
        //修改套餐基本信息
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);

        Long setmealId=setmealDTO.getId();



        //重新插入新的关联关系
        List<SetmealDish> setmealDishes=setmealDTO.getSetmealDishes();
        if(setmealDishes !=null && setmealDishes.size()>0){
            //删除原有的套餐--菜品关联
            setmealDishMapper.deleteBySetmealId(setmealId);

            setmealDishes.forEach(setmealDish->{
                setmealDish.setSetmealId(setmealId);
            });
            setmealDishMapper.insertBatch(setmealDishes);
        }

    }

    public void deleteBatch(List<Long> ids){
        //校验在售套餐不能删除
        for(Long id:ids){
            Setmeal setmeal=setmealMapper.getById(id);
            if(setmeal!=null& StatusConstant.ENABLE.equals(setmeal.getStatus())){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        //删除套餐和菜品的关联数据
        setmealDishMapper.deleteBySetmealIds(ids);

        //删除套餐中的数据
        setmealMapper.deleteByids(ids);

    }

    public void startOrStop(Integer status, Long id) {
        //起售套餐时,校验套餐内是否包含未起售的菜品
        if (StatusConstant.ENABLE.equals(status)) {
            List<Dish> dishes = dishMapper.getBySetmealId(id);
            if (dishes != null && dishes.size() > 0) {
                for (Dish dish : dishes) {
                    if (StatusConstant.DISABLE.equals(dish.getStatus())) {
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                }
            }
        }

        //更新套餐状态
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setStatus(status);
        setmealMapper.update(setmeal);
    }

    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }

}
