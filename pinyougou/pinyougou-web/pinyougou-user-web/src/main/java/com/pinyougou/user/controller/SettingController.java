package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.service.AreasService;
import com.pinyougou.service.CitiesService;
import com.pinyougou.service.ProvincesService;
import com.pinyougou.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/setting")
public class SettingController {

    @Reference(timeout = 10000)
    private UserService userService;
    @Reference(timeout = 10000)
    private ProvincesService provincesService;
    @Reference(timeout = 10000)
    private CitiesService citiesService;
    @Reference(timeout = 10000)
    private AreasService areasService;

    // 查找省份
    @GetMapping("/findProvinces")
    public List<Provinces> findProvinces() {

        List<Provinces> provincesList = provincesService.findAll();
        return provincesList;
    }


    @GetMapping("/findCities")
    public List<Cities> findCities(String provinceId) {

        List<Cities> cityiesList = citiesService.findCitiesByProvinceId(provinceId);
        return cityiesList;
    }

    @GetMapping("/findAreas")
    public List<Areas> findAreas(String cityId) {

        List<Areas> areasList = areasService.findAreasByCitiesId(cityId);
        return areasList;
    }


    @GetMapping("/findPhone")
    public String findPhone(HttpServletRequest request) {

            String username = request.getRemoteUser();
            return userService.findPhone(username);
    }
}