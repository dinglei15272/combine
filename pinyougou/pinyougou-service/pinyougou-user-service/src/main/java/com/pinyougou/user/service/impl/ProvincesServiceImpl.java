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
    /**
     * 添加方法
     *
     * @param provinces
     */
    @Override
    public void save(Provinces provinces) {

    }

    /**
     * 修改方法
     *
     * @param provinces
     */
    @Override
    public void update(Provinces provinces) {

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
    public Provinces findOne(Serializable id) {
        return null;
    }

    /**
     * 查询全部
     */
    @Override
    public List<Provinces> findAll() {
        try {

            List<Provinces> provinces = provincesMapper.selectAll();
            return provinces;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
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
}
