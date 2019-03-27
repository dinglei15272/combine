package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.common.util.HttpClientUtils;
import com.pinyougou.mapper.UserMapper;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务接口实现类
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-03-16<p>
 */
@Service(interfaceName = "com.pinyougou.service.UserService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${sms.url}")
    private String smsUrl;
    @Value("${sms.signName}")
    private String signName;
    @Value("${sms.templateCode}")
    private String templateCode;

    @Override
    public void save(User user) {
        try{
            // 密码需要MD5加密 commons-codec-xxx.jar
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            // 创建时间
            user.setCreated(new Date());
            // 修改时间
            user.setUpdated(user.getCreated());
            userMapper.insertSelective(user);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    //更新用户数据
    @Override
    public void update(User user) {
        try {
            //定义一个模板
            Example example = new Example(User.class);
            //创建条件对象
            Example.Criteria criteria = example.createCriteria();
            //添加条件
            criteria.andEqualTo("username",user.getUsername());
            //加入条件查询开始查询
//            User userid= (User) userMapper.selectByExample(example);
            //修改ID完成添加
//            user.setId(userid.getId());
            //完成修改
//            userMapper.updateByPrimaryKeySelective(user);
            userMapper.updateByExampleSelective(user,example);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    /** 以用户名查询 */
    @Override
    public User findName(User user) {
        try {
            //定义一个模板
            Example example = new Example(User.class);
            //创建条件对象
            Example.Criteria criteria = example.createCriteria();
            //添加条件
            criteria.andEqualTo("username",user.getUsername());
            //加入条件查询开始查询
//            User userid= (User) userMapper.selectByExample(example);
            //修改ID完成添加
//            user.setId(userid.getId());
            //完成修改
//            userMapper.updateByPrimaryKeySelective(user);
            List<User> users =userMapper.selectByExample(example);
            return users.get(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findOne(Serializable id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findByPage(User user, int page, int rows) {
        return null;
    }

    /** 发送短信验证码 */
    public boolean sendSmsCode(String phone){
        try{
            // 1. 生成6位随机数字的验证码
            String code = UUID.randomUUID().toString().replaceAll("-","")
                    .replaceAll("[a-zA-Z]","").substring(0,6);
            System.out.println("code: " + code);

            // 2. 调用短信发送接口
            // 创建 HttpClientUtils对象
            HttpClientUtils httpClientUtils = new HttpClientUtils(false);
            // 定义Map集合封装请求参数
            Map<String, String> params = new HashMap<>();
            params.put("phone", phone);
            params.put("signName", signName);
            params.put("templateCode", templateCode);
            params.put("templateParam", "{'code':'"+ code +"'}");
            // 调用短信接口
            String content = httpClientUtils.sendPost(smsUrl, params);
            System.out.println(content);

            // 3. 如果发送成功，把验证码存储到Redis数据库
            Map map = JSON.parseObject(content, Map.class);
            boolean success = (boolean)map.get("success");
            if (success){
                // 把验证码存储到Redis数据库，有效期90秒
                redisTemplate.boundValueOps(phone).set(code, 90, TimeUnit.SECONDS);
            }
            return true;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 检验短信验证码 */
    public boolean checkSmsCode(String phone, String code){
        try{
            // 从Redis数据库获取短信验证码
            String oldCode = (String)redisTemplate.boundValueOps(phone).get();
            return oldCode != null && oldCode.equals(code);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void setSafe(User user) {
        try {
            User user1 = userMapper.findUserByUserName(user.getUsername());
            user1.setPassword(DigestUtils.md5Hex(user.getPassword()));
            user1.setNickName(user.getNickName());
            userMapper.updateByPrimaryKey(user1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePhone(String phone,String username) {
        try{
            User user1 = userMapper.findUserByUserName(username);
            user1.setPhone(phone);
            userMapper.updateByPrimaryKey(user1);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String findPhone(String username) {
        try{
            User user = userMapper.findUserByUserName(username);
            return user.getPhone();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
