package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.Dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;

import java.util.List;

/**
 * The interface Dish service.
 *
 * @Author wild
 * @Date 2022 /4/19 0019 下午 14:31
 * @Version 1.0
 */
public interface DishService extends IService<Dish> {
    /**
     * Save with flavor.
     *
     * @param dishDto the dish dto
     */
    void saveWithFlavor(DishDto dishDto);

    /**
     * Update by id dish dto.
     * 根据Id修改
     * 菜品数据回显
     *
     * @param id the dish dto
     * @return the dish dto
     */
    DishDto updateById(Long id);

    /**
     * Update with flavor.
     * 修改菜品数据
     *
     * @param dishDto the dish dto
     */
    void updateWithFlavor(DishDto dishDto);

    /**
     * Delete by id.
     *删除菜品
     * @param ids the id
     */
   void deleteById(List<Long> ids);


//
}