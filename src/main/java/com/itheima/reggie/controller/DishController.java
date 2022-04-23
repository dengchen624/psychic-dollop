package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.Dto.DishDto;
import com.itheima.reggie.common.R;

import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Dish controller.
 *
 * @Author wild
 * @Date 2022 /4/20 0020 上午 11:20
 * @Version 1.0
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;


    /**
     * Save r.
     *
     * @param dishDto the dish dto
     * @return the r
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * Page r r.
     * 菜品查询
     *
     * @param page     the page
     * @param pageSize the page size
     * @param name     the name
     * @return the r
     */
    @GetMapping("/page")
    public R<Page> pageR(int page, int pageSize, String name) {

        log.info("page={},pageSize={},name={}", page, pageSize, name);
//        添加分页构造器
        Page<Dish> pageInfo = new Page(page, pageSize);
        Page<DishDto> dtoPage = new Page<>();
//        构造条件构造器
        LambdaQueryWrapper<Dish> QueryWrapper = new LambdaQueryWrapper();
        //  添加过滤条件
        QueryWrapper.like(name != null, Dish::getName, name);
//        添加排序条件
        QueryWrapper.orderByDesc(Dish::getUpdateTime);
//        执行查询
        dishService.page(pageInfo, QueryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);


    }

    /**
     * Update r.
     * 数据回显
     *
     * @param id the id
     * @return the r
     */
    @GetMapping("/{id}")
    public R<DishDto> update(@PathVariable Long id) {

        DishDto dishDto = dishService.updateById(id);
        return R.success(dishDto);
    }

    /**
     * Update by id r.
     * 保存
     *
     * @param dishDto the dish dto
     * @return the r
     */
    @PutMapping
    public R<String> updateById(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * Delete by ids r.
     * 删除菜品和批量删除
     *
     * @param ids the ids
     * @return the r
     */
    @DeleteMapping
    public R<String> deleteByIds(@RequestParam List<Long> ids) {

        dishService.deleteById(ids);
        return R.success("删除成功");
    }


    /**
     * Update status r.
     *
     * @param ids    the ids
     * @param status the status
     * @return the r
     */
    @PostMapping("/status/{status}")
    public R<List<Dish>> updateStatus(@RequestParam List<Long> ids, @PathVariable Integer status) {
        log.info("status={} ids={}", status, ids);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids);
        List<Dish> list = dishService.list(queryWrapper);
        List<Dish> dishList = list.stream().map((item) -> {
            item.setStatus(status);
            return item;
        }).collect(Collectors.toList());
        dishService.updateBatchById(dishList);
        return R.success(dishList);
    }


    /**
     * List r.
     * 根据条件查询对于菜品数据；
     *
     * @param dish the dish
     * @return the r
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        //        构造条件构造器
        LambdaQueryWrapper<Dish> QueryWrapper = new LambdaQueryWrapper();
        QueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        QueryWrapper.eq(Dish::getStatus, 1);
        //        添加排序条件
        QueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(QueryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);

            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }
}
