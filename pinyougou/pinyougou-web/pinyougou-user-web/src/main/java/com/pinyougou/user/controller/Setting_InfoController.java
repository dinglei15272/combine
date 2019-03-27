package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.pojo.User;
import com.pinyougou.service.AreasService;
import com.pinyougou.service.CitiesService;
import com.pinyougou.service.ProvincesService;
import com.pinyougou.service.UserService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/info")
public class Setting_InfoController {

    @Reference(timeout = 10000)
    private UserService userService;
    @Reference(timeout = 10000)
    private ProvincesService provincesService;
    @Reference(timeout = 10000)
    private CitiesService citiesService;
    @Reference(timeout = 10000)
    private AreasService areasService;

    //获得数据
      @GetMapping("/province")
    public List<Provinces> province (@RequestParam(value = "parentId") Long parentId){
            return provincesService.findAll();
    }

    //获得数据
      @GetMapping("/city")
    public List<Cities> city (@RequestParam(value = "parentId") Long parentId){
            return citiesService.findItemCatByParentId(Long.valueOf(provincesService.findOne(parentId).getProvinceId()));
    }

    //获得数据
      @GetMapping("/area")
    public List<Areas> area (@RequestParam(value = "parentId") Long parentId){
         return areasService.findItemCatByParentId(Long.valueOf(citiesService.findOne(parentId).getCityId()));
    }

    //更新用户信息
    @PostMapping("/update")
    public boolean update (@RequestBody User user){
        try {
            //获得用户名
            SecurityContext context = SecurityContextHolder.getContext();
            String loginName = context.getAuthentication().getName();
            //修改用户名
            user.setUsername(loginName);
            //执行修改
            userService.update(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
