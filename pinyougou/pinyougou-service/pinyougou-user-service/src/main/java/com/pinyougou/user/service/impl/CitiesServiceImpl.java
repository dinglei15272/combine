package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.CitiesMapper;
import com.pinyougou.pojo.Cities;
import com.pinyougou.service.CitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service(interfaceName = "com.pinyougou.service.CitiesService")
@Transactional
public class CitiesServiceImpl implements CitiesService {

    @Autowired
    private CitiesMapper citiesMapper;
    /**
     * 添加方法
     *
     * @param cities
     */
    @Override
    public void save(Cities cities) {

    }

    /**
     * 修改方法
     *
     * @param cities
     */
    @Override
    public void update(Cities cities) {

    }

    /**
     * 根据主键id删除
     *
     * @param id
     */
    @Override
    public void delete(Serializable id) {

    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Override
    public void deleteAll(Serializable[] ids) {

    }

    /**
     * 根据主键id查询
     *
     * @param id
     */
    @Override
    public Cities findOne(Serializable id) {
        return null;
    }

    /**
     * 查询全部
     */
    @Override
    public List<Cities> findAll() {
        return null;
    }

    /**
     * 多条件分页查询
     *
     * @param cities
     * @param page
     * @param rows
     */
    @Override
    public List<Cities> findByPage(Cities cities, int page, int rows) {
        return null;
    }

    @Override
    public List<Cities> findCitiesByProvinceId(String provinceId) {
        try{
            return citiesMapper.findCitiesByProvinceId(provinceId);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
