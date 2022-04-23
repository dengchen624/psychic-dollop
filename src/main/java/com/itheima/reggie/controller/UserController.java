package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.Dto.UserDto;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.SMSUtils;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author wild
 * @Date 2022/4/21 0021 下午 15:08
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/sendMsg")
    public R<String> sendMsg(HttpSession session, @RequestBody User user) {
        String phone = user.getPhone();

        String code = ValidateCodeUtils.generateValidateCode(4).toString();
        log.info("code={}",code);
//        SMSUtils.sendMessage("瑞吉外卖", "", phone, code);
       session.setAttribute(phone, code);
        return R.success("短信发送成功");
    }


    @PostMapping("/login")
    public R<User> login(HttpSession session, @RequestBody UserDto userDto) {
        String phone = userDto.getPhone();
        String code = userDto.getCode();
        log.info("code={}", code);
        Object codeSession = session.getAttribute(phone);
        if (codeSession != null && codeSession.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                 user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);

            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登陆失败");
    }
}