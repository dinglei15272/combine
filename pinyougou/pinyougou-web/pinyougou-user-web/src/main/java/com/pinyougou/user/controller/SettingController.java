package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/setting")
public class SettingController {

    @Reference(timeout = 10000)
    private UserService userService;

    @GetMapping("/findPhone")
    public String findPhone(HttpServletRequest request) {

            String username = request.getRemoteUser();
            return userService.findPhone(username);
    }
}