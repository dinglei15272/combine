package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.SellerMapper;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceName = "com.pinyougou.service.PasswordService")
@Transactional
public class PasswordImpl implements PasswordService {

    @Autowired
    private SellerMapper sellerMapper;

    //    修改密码
    @Override
    public void updatepassword(Seller seller) {
        try {
            //修改密码
            sellerMapper.updateByPrimaryKeySelective(seller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String selectpassword(String password) {
        return null;
    }


}
