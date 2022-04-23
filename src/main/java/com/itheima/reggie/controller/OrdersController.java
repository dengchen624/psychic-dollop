package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wild
 * @Date 2022/4/22 0022 下午 20:53
 * @Version 1.0
 */
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public R<String> submit(Orders orders) {
        orderService.submit(orders);
        return R.success("提交成功");
    }

}
