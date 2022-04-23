package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.Dto.DishDto;
import com.itheima.reggie.Dto.SetmealDto;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetMealDishService;
import com.itheima.reggie.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Set meal controller.
 *
 * @Author wild
 * @Date 2022 /4/20 0020 下午 22:24
 * @Version 1.0
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetMealController {
    @Autowired
    private SetMealDishService setMealDishService;
    @Autowired
    private SetMealService setMealService;
    @Autowired
    private CategoryService categoryService;

    /**
     * Save r.
     * 新增套餐数据
     *
     * @param setmealDto the setmeal dto
     * @return the r
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info(setmealDto.toString());
        setMealService.saveWithCategory(setmealDto);
        return R.success("新增成功");
    }

    /**
     * Page r r.
     * 分页查询
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
        Page<Setmeal> pageInfo = new Page(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
//        构造条件构造器
        LambdaQueryWrapper<Setmeal> QueryWrapper = new LambdaQueryWrapper();
        //  添加过滤条件
        QueryWrapper.like(name != null, Setmeal::getName, name);
//        添加排序条件
        QueryWrapper.orderByDesc(Setmeal::getUpdateTime);
//        执行查询
        setMealService.page(pageInfo, QueryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((items) -> {
            SetmealDto setmealDto = new SetmealDto();

            BeanUtils.copyProperties(items, setmealDto);

            Long categoryId = items.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);


    }

    /**
     * Delete by ids r.
     *批量删除
     * @param ids the ids
     * @return the r
     */
    @DeleteMapping
    public R<String> deleteByIds(@RequestParam List<Long> ids) {

        setMealService.deleteById(ids);
        return R.success("删除成功");
    }

    /**
     * Update status r.
     * 批量启售和禁售
     *
     * @param ids    the ids
     * @param status the status
     * @return the r
     */
    @PostMapping("/status/{status}")
    public R<List<Setmeal>> updateStatus(@RequestParam List<Long> ids, @PathVariable Integer status) {
        log.info("status={} ids={}", status, ids);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        List<Setmeal> list = setMealService.list(queryWrapper);
        List<Setmeal> dishList = list.stream().map((item) -> {
            item.setStatus(status);
            return item;
        }).collect(Collectors.toList());
        setMealService.updateBatchById(dishList);
        return R.success(dishList);
    }


    /**
     * List r r.
     *前台展示套餐数据
     * @param setmeal the setmeal
     * @return the r
     */
    @GetMapping("/list")
    public R<List<Setmeal>> listR(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setMealService.list(queryWrapper);
        return R.success(list);
    }
}
