package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.mapper.orderDetailMapper;
import com.itheima.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @Author wild
 * @Date 2022/4/22 0022 下午 20:46
 * @Version 1.0
 */
@Service
public class OrderDetailServiceImpl  extends ServiceImpl<orderDetailMapper, OrderDetail> implements OrderDetailService {
}
