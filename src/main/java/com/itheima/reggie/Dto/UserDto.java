package com.itheima.reggie.Dto;

import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.entity.User;
import lombok.Data;

import java.util.List;

/**
 * @Author wild
 * @Date 2022/4/21 0021 下午 16:22
 * @Version 1.0
 */
@Data
public class UserDto extends User {

    private String code;
}
