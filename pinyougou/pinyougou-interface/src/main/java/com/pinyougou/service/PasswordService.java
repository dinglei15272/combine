package com.pinyougou.service;

import com.pinyougou.pojo.Seller;

/**
 * PasswordService 服务接口
 * @date 2019-02-27 10:03:32
 * @version 1.0
 */
public interface PasswordService {
    /** 修改方法 */
    void updatepassword(Seller seller);

    /** 密码查询 */
    String selectpassword(String password);
}
