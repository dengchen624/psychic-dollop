package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.Dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author wild
 * @Date 2022/4/19 0019 下午 14:31
 * @Version 1.0
 */
@Service
public interface SetMealService extends IService<Setmeal> {
    void saveWithCategory(SetmealDto setmealDto);
    void deleteById(List<Long> ids);
}
