package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;

/**
 * The interface Category service.
 *
 * @Author wild
 * @Date 2022 /4/19 0019 下午 14:31
 * @Version 1.0
 */
public interface CategoryService  extends IService<Category> {
    /**
     * Remove.
     *
     * @param id the id
     */
    void remove(Long id);
}
