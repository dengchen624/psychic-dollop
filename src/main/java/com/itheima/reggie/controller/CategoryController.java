package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;

import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Category controller.
 *
 * @Author wild
 * @Date 2022 /4/19 0019 下午 14:34
 * @Version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Save r.
     * 新增
     *
     * @param category the category
     * @return the r
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("category:{}", category);
        categoryService.save(category);
        return R.success("新增成功");
    }

    /**
     * Page r r.
     * 分页查询
     *
     * @param page     the page
     * @param pageSize the page size
     * @return the r
     */
    @GetMapping("/page")
    public R<Page> pageR(int page, int pageSize) {
        log.info("page={},pageSize={},name={}", page, pageSize);
//        添加分页构造器
        Page<Category> pageInfo = new Page(page, pageSize);
//        构造条件构造器
        LambdaQueryWrapper<Category> QueryWrapper = new LambdaQueryWrapper();
//        添加排序条件
        QueryWrapper.orderByAsc(Category::getSort);
//        执行查询
        categoryService.page(pageInfo, QueryWrapper);
        return R.success(pageInfo);
    }

    /**
     * Delete r.
     * 删除
     *
     * @param ids the ids
     * @return the r
     */
    @DeleteMapping
    public R<String> delete(Long ids) {
        categoryService.remove(ids);
        return R.success("删除成功");
    }


    /**
     * Update r.
     * 修改分类
     *
     * @param category the category
     * @return the r
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("修改成功");
    }


    /**
     * List r.
     * 根据条件查询菜品分类
     *
     * @param category the category
     * @return the r
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        //        构造条件构造器
        LambdaQueryWrapper<Category> QueryWrapper = new LambdaQueryWrapper();
        QueryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        //        添加排序条件
        QueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(QueryWrapper);
        return R.success(list);
    }

}
