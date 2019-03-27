package com.pinyougou.service;

import com.pinyougou.pojo.Item;

import java.io.Serializable;
import java.util.Map;

public interface Setting_InfoService {
    /** 添加方法 */
    void save(Item item);

    /** 修改方法 */
    void update(Item item);

    /** 根据主键id删除 */
    void delete(Serializable id);

    /** 批量删除 */
    void deleteAll(Serializable[] ids);

    /** 根据主键id查询 */
    Item findOne(Serializable id);

    /** 查询全部 */
    Map<String,Object> findAll();
}
