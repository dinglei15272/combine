package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.CitiesMapper;
import com.pinyougou.pojo.Cities;
import com.pinyougou.service.CitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

<<<<<<< HEAD
@Service(interfaceName = "com.pinyougou.service.CitiesService")
=======
@Service(interfaceName = "com.pinyougou.service.CitiesService" )
>>>>>>> 4eaae376272e8456a9462dc56e7e97df7a5b4314
@Transactional
public class CitiesServiceImpl implements CitiesService {

    @Autowired
    private CitiesMapper citiesMapper;
<<<<<<< HEAD
    /**
     * 添加方法
     *
     * @param cities
     */
=======

>>>>>>> 4eaae376272e8456a9462dc56e7e97df7a5b4314
    @Override
    public void save(Cities cities) {

    }

<<<<<<< HEAD
    /**
     * 修改方法
     *
     * @param cities
     */
=======
>>>>>>> 4eaae376272e8456a9462dc56e7e97df7a5b4314
    @Override
    public void update(Cities cities) {

    }

<<<<<<< HEAD
    /**
     * 根据主键id删除
     *
     * @param id
     */
=======
>>>>>>> 4eaae376272e8456a9462dc56e7e97df7a5b4314
    @Override
    public void delete(Serializable id) {

    }

<<<<<<< HEAD
    /**
     * 批量删除
     *
     * @param ids
     */
=======
>>>>>>> 4eaae376272e8456a9462dc56e7e97df7a5b4314
    @Override
    public void deleteAll(Serializable[] ids) {

    }

<<<<<<< HEAD
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
=======
    @Override
    public Cities findOne(Serializable id) {
        try {
            return citiesMapper.selectone(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Cities> findAll() {
        return citiesMapper.selectAll();
    }

>>>>>>> 4eaae376272e8456a9462dc56e7e97df7a5b4314
    @Override
    public List<Cities> findByPage(Cities cities, int page, int rows) {
        return null;
    }

    @Override
<<<<<<< HEAD
    public List<Cities> findCitiesByProvinceId(String provinceId) {
        try{
            return citiesMapper.findCitiesByProvinceId(provinceId);
        }catch (Exception e){
            throw new RuntimeException(e);
=======
    public List<Cities> findItemCatByParentId(Long parentId) {
        try{
            return citiesMapper.findItemCatByParentId(parentId);
        }catch (Exception ex){
            throw new RuntimeException(ex);
>>>>>>> 4eaae376272e8456a9462dc56e7e97df7a5b4314
        }
    }
}
