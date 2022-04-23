package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.Dto.SetmealDto;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetMealDishService;
import com.itheima.reggie.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author wild
 * @Date 2022/4/19 0019 下午 14:32
 * @Version 1.0
 */
@Service
public class SetMealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetMealService {
    @Autowired
    private SetMealDishService setMealDishService;

    @Override
    public void saveWithCategory(SetmealDto setmealDto) {
        //存储套餐基本信息
        this.save(setmealDto);
        //存储关联菜品信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
           item.setSetmealId(setmealDto.getId());
           return item;
        }).collect(Collectors.toList());
        setMealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void deleteById(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId, ids);
        lambdaQueryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(lambdaQueryWrapper);
        if (count > 0) {
            throw new CustomException("套餐正在售卖，不能删除");
        }
        //删除菜品信息
        this.removeByIds(ids);
        //删除口味信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getDishId, ids);
        setMealDishService.remove(queryWrapper);
    }
}
