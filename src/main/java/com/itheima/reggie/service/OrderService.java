package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Orders;
import org.springframework.stereotype.Service;

/**
 * @Author wild
 * @Date 2022/4/22 0022 下午 20:44
 * @Version 1.0
 */
@Service
public interface OrderService extends IService<Orders> {
    public void submit(Orders orders);
}
