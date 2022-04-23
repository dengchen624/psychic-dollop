package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author wild
 * @Date 2022/4/21 0021 下午 15:05
 * @Version 1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
