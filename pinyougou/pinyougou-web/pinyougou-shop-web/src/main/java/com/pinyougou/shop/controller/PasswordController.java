package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.PasswordService;
import com.pinyougou.service.SellerService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RequestMapping("/password")
@RestController
public class PasswordController {
    @Reference(timeout = 10000)
    private PasswordService passwordService;

    @Reference(timeout = 10000)
    private SellerService sellerService;

    @PostMapping("/update")
    public boolean update(@RequestBody Map<String, Object> passwords, HttpServletRequest request) {
        try {
            //获得用户名
            String name = request.getRemoteUser();
            //BCryptPasswordEncoder 加密类
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //获得用户数据
            Seller seller = new Seller();
            seller.setSellerId(name);
            seller.setPassword(sellerService.findOne(name).getPassword());
            //校对旧密码
            if (passwordEncoder.matches((CharSequence) passwords.get("old"), seller.getPassword())) {
                //加入前端加密后的密码
                seller.setPassword(passwordEncoder.encode((CharSequence) passwords.get("new01")));
                passwordService.updatepassword(seller);
                HttpSession session1 = request.getSession();
                session1.invalidate();
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
