package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * The type Employee controller.
 *
 * @author wild
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request  the request
     * @param employee the employee
     * @return r r
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3、如果没有查询到则返回登录失败结果
        if (emp == null) {
            return R.error("登录失败");
        }

        //4、密码比对，如果不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }

        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        //6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     *
     * @param request the request
     * @return r r
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清理Session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * Save r.
     *
     * @param request  the request
     * @param employee the employee
     * @return the r
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("接收到的参数：{}", employee.toString());
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employeeService.save(employee);
        return R.success("添加成功");

    }

    /**
     * Page r r.
     *
     * @param page     the page
     * @param pageSize the page size
     * @param name     the name
     * @return the r
     */
    @GetMapping("/page")
    public R<Page> pageR(int page, int pageSize, String name) {
        log.info("page={},pageSize={},name={}", page, pageSize, name);
//        添加分页构造器
        Page pageInfo = new Page(page, pageSize);
//        构造条件构造器
        LambdaQueryWrapper<Employee> QueryWrapper = new LambdaQueryWrapper();
//        添加过滤条件
        QueryWrapper.like(StringUtils.hasText(name), Employee::getName, name);
//        添加排序条件
        QueryWrapper.orderByDesc(Employee::getUpdateTime);
//        执行查询
        employeeService.page(pageInfo, QueryWrapper);
        return R.success(pageInfo);
    }

    /**
     * Update r.
     *
     * @param request  the request
     * @param employee the employee
     * @return the r
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());

        long emp = (long) request.getSession().getAttribute("employee");
        long id = Thread.currentThread().getId();
        log.info("线程id为{}", id);
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(emp);
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");

    }

    /**
     * Update by id r.
     *
     * @param id the id
     * @return the r
     */
    @GetMapping("/{id}")
    public R<Employee> updateById(@PathVariable Long id) {
        log.info("根据id查询员工信息--",id);
        Employee byId = employeeService.getById(id);
        if (byId != null) {
            return R.success(byId);
        }
        return R.error("修改失败");
    }

}