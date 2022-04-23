package com.itheima.reggie.controller;

import com.itheima.reggie.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wild
 * @Date 2022/4/22 0022 下午 20:54
 * @Version 1.0
 */
@RestController
@RequestMapping("/orderDetail")
public class OrderDetail {
    @Autowired
    private OrderDetailService orderDetailService;
}
