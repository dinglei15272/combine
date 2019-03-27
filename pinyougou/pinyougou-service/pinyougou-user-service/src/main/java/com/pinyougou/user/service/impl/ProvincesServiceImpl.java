package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.ProvincesMapper;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.service.ProvincesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service(interfaceName = "com.pinyougou.service.ProvincesService")
@Transactional
public class ProvincesServiceImpl implements ProvincesService {

    @Autowired
    private ProvincesMapper provincesMapper;

    @Override
    public void save(Provinces provinces) {

    }

    @Override
    public void update(Provinces provinces) {

    }


    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    /**
     * 根据主键id查询
     *
     * @param id
     */
    @Override
    public Provinces findOne(Serializable id) {
        try {
            return provincesMapper.selectone(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询全部
     */
    @Override
    public List<Provinces> findAll() {
        return provincesMapper.selectAll();
    }

    /**
     * 多条件分页查询
     *
     * @param provinces
     * @param page
     * @param rows
     */
    @Override
    public List<Provinces> findByPage(Provinces provinces, int page, int rows) {
        return null;
    }

    /**
     * 按ID来多查询
     *
     * @param parentId
     */
    @Override
    public List<Provinces> findItemCatByParentId(Long parentId) {
        return null;
    }


}
