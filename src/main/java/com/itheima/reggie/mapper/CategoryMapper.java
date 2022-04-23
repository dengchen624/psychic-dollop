package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author wild
 * @Date 2022/4/19 0019 下午 14:31
 * @Version 1.0
 */
@Mapper
public interface CategoryMapper  extends BaseMapper<Category> {
}
