package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author wild
 * @Date 2022/4/22 0022 下午 20:42
 * @Version 1.0
 */
@Mapper
public interface orderMapper extends BaseMapper<Orders> {
}
