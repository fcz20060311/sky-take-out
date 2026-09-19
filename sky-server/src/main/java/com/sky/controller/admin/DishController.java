package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Api(tags="菜品相关接口")
@RestController
@Slf4j
@RequestMapping("/admin/dish")
public class DishController {


    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("新增菜品")
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO){
       log.info("新增菜品:{}",dishDTO);
       dishService.save(dishDTO);
       // 新增的菜品还没进任何套餐，只需清这个分类的菜品缓存
       cleanCache("dish_"+dishDTO.getCategoryId());

       return Result.success();
    }

    @ApiOperation("分页查询菜品")
    @GetMapping("/page")
    public Result<PageResult> pagequery(DishPageQueryDTO dishPageQueryDTO){
        log.info("分页查询菜品:{}",dishPageQueryDTO);
        PageResult page=dishService.pagequery(dishPageQueryDTO);
        return Result.success(page);
    }

    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result dishdelete(@RequestParam List<Long> ids){
        log.info("批量删除菜品:{}",ids);
        dishService.deleteids(ids);
        // 删除可能涉及多个分类，直接全清
        cleanCache("dish_*");
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据id查询菜品:{}",id);
        DishVO dishVO=dishService.getById(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    // 菜品的图片和描述会出现在套餐详情里，所以套餐菜品缓存也要清
    @CacheEvict(cacheNames = "setmealDishCache", allEntries = true)
    public Result update(@RequestBody DishDTO dishDTO){

        log.info("修改菜品:{}",dishDTO);
        dishService.update(dishDTO);
        // 菜品可能换了分类，直接全清，避免旧分类的缓存清不掉
        cleanCache("dish_*");

        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("菜品启售停售")
    public Result<String> startorStop(@PathVariable Integer status,Long id){
        log.info("菜品启售停售:{}",id);

        // 顺序不能反：先改数据库，再清缓存
        dishService.startOrStop(status,id);
        cleanCache("dish_*");
        return Result.success();
    }

    private void cleanCache(String pattern){
        Set keys=redisTemplate.keys(pattern);
        // 没匹配到 key 时直接返回，避免把空集合传给 delete
        if(keys!=null&&!keys.isEmpty()){
            redisTemplate.delete(keys);
        }
    }
}
