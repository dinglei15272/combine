package com.pinyougou.service;

import com.pinyougou.pojo.User;

import java.io.Serializable;
import java.util.List;

/**
 * UserService 服务接口
 * @date 2019-02-27 10:03:32
 * @version 1.0
 */
public interface UserService {

	/** 添加方法 */
	void save(User user);

	/** 修改方法 */
	void update(User user);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 以用户名查询 */
	User findName(User name);

	/** 根据主键id查询 */
	User findOne(Serializable id);

	/** 查询全部 */
	List<User> findAll();

	/** 多条件分页查询 */
	List<User> findByPage(User user, int page, int rows);

	/** 发送短信验证码 */
	boolean sendSmsCode(String phone);

	/** 检验短信验证码 */
	boolean checkSmsCode(String phone, String code);

	void setSafe(User user);

	void updatePhone(String phone,String username);

	String findPhone(String username);
}