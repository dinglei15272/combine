package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Address;
import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.service.AddressService;
import com.pinyougou.service.AreasService;
import com.pinyougou.service.CitiesService;
import com.pinyougou.service.ProvincesService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/setting")
public class settingController {

    @Reference(timeout = 10000)
    private AddressService addressService;

    @Reference(timeout = 10000)
    private ProvincesService provincesService;

    @Reference(timeout = 10000)
    private CitiesService citiesService;

    @Reference(timeout = 10000)
    private AreasService areasService;
	
	@Reference(timeout = 10000)
    private UserService userService;

    @GetMapping("/findPhone")
    public String findPhone(HttpServletRequest request) {

            String username = request.getRemoteUser();
            return userService.findPhone(username);
    }

    /**
     * 获取登录用户的地址列表
     */
    @GetMapping("/findAddressByUser")
    public List<Address> findAddressByUser(HttpServletRequest request) {
        // 获取登录用户名
        String userId = request.getRemoteUser();
        return addressService.findAddressByUser(userId);
    }


    @GetMapping("/province")
    public List<Provinces> findProvince(@RequestParam(value = "tableId") Long tableId) {
        return provincesService.findAll();
    }

    @GetMapping("/city")
    public List<Cities> findCity(@RequestParam(value = "tableId") Long tableId) {
        return citiesService.findByProvinceId(tableId);
    }

    @GetMapping("/area")
    public List<Areas> findArea(@RequestParam(value = "tableId") Long tableId) {
        return areasService.findByCityId(tableId);
    }

    @PostMapping("/save")
    public boolean save(@RequestBody Address address) {
        try {
            String addressId = SecurityContextHolder.getContext().getAuthentication().getName();
            address.setUserId(addressId);
            addressService.save(address);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @GetMapping("/delete")
    public boolean delete(Long id) {
        try {
            addressService.delete(id);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @GetMapping("/saveDefault")
    public boolean saveDefault(Long id,String st) {
        try {
            Address address = new Address();
            String addressId = SecurityContextHolder.getContext().getAuthentication().getName();
            address.setUserId(addressId);
            if (st.equals("0")) {
                address.setIsDefault("1");
            } else {
                address.setIsDefault("0");
            }
            address.setId(id);
            addressService.save(address);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

