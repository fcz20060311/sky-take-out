package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@Api(tags="套餐相关接口")
@RequestMapping("/admin/setmeal")
public class SetmealController{

    @Autowired
    SetmealService setmealService;


    @ApiOperation("分页查询套餐")
    @GetMapping("/page")
    public Result<PageResult> pagequery(SetmealPageQueryDTO setmealPageQueryDTO){

        log.info("分页查询套餐:{}",setmealPageQueryDTO);
        PageResult page= setmealService.pageQuery(setmealPageQueryDTO);

        return Result.success(page);
    }

    @ApiOperation("新增套餐")
    @PostMapping
    // 新增后这个分类的套餐列表变了，清掉 C 端缓存
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐:{}",setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();
    }

    @ApiOperation("根据id查询套餐")
    @GetMapping("/{id}")
    public Result <SetmealVO> getById(@PathVariable Long id){
        log.info("根据id查询套餐:{}",id);
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    @ApiOperation("修改套餐")
    @PutMapping
    // 分类和套餐内的菜品都可能变，两个缓存区一起清
    @CacheEvict(cacheNames = {"setmealCache", "setmealDishCache"}, allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐:{}",setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    @ApiOperation("批量删除套餐")
    @DeleteMapping
    // 套餐没了，它的列表缓存和菜品详情缓存都失效
    @CacheEvict(cacheNames = {"setmealCache", "setmealDishCache"}, allEntries = true)
    public Result delete(@RequestParam List<Long> ids){
        log.info("批量删除套餐:{}",ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    @ApiOperation("套餐起售、停售")
    @PostMapping("/status/{status}")
    // 在售状态变了，C 端列表要重新查
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result startOrStop(@PathVariable Integer status, Long id){
        log.info("套餐起售、停售:{},{}",status,id);
        setmealService.startOrStop(status,id);
        return Result.success();
    }

}
