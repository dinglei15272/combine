package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.CitiesMapper;
import com.pinyougou.pojo.Cities;
import com.pinyougou.service.CitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.List;

@Service(interfaceName = "com.pinyougou.service.CitiesService")
@Transactional
public class CitiesServiceImpl implements CitiesService {

    @Autowired
    private CitiesMapper citiesMapper;
    @Override
    public void save(Cities cities) {

    }

    @Override
    public void update(Cities cities) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public Cities findOne(Serializable id) {
        try {
            return citiesMapper.selectone(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询全部
     */
    @Override
    public List<Cities> findAll() {
        return null;
    }

    public List<Cities> findByPage(Cities cities, int page, int rows) {
        return null;
    }

    @Override
    public List<Cities> findItemCatByParentId(Long parentId) {
        try{
            return citiesMapper.findItemCatByParentId(parentId);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
	
	@Override
    public List<Cities> findByProvinceId(Long provinceId) {
        Example example =  new Example(Cities.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("provinceId", provinceId);
        return citiesMapper.selectByExample(example);
    }

}
