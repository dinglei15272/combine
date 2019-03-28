package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-03-18<p>
 */
@RestController
public class LoginController {

    @Reference(timeout = 10000)
    private UserService userService;

    /**
     * 获取登录用户名和头像以及其他信息
     */
    @GetMapping("/user/showUser")
    public Map<String, Object> showUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        String loginName = context.getAuthentication().getName();
        Map<String, Object> data = new HashMap<>();
        try {
            User user = new User();
            user.setUsername(loginName);
            user = userService.findName(user);
            JSONObject jsonObject = JSONObject.parseObject(user.getAddress());
            data.putAll(jsonObject);
            data.put("User", user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        data.put("loginName", loginName);
        return data;
    }

    @GetMapping("/user/showName")
    public Map<String, Object> showName() {
        SecurityContext context = SecurityContextHolder.getContext();
        String loginName = context.getAuthentication().getName();
        Map<String, Object> data = new HashMap<>();
        data.put("loginName", loginName);
        return data;
    }


}
