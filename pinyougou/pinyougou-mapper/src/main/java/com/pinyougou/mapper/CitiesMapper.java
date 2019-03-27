package com.pinyougou.mapper;

import com.pinyougou.pojo.Cities;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.io.Serializable;
import java.util.List;

/**
 * CitiesMapper 数据访问接口
 * @date 2019-02-27 09:55:07
 * @version 1.0
 */
public interface CitiesMapper extends Mapper<Cities>{

    @Select("SELECT * FROM tb_cities WHERE provinceid = #{parentId}")
    List<Cities> findItemCatByParentId(Long parentId);

    @Select("SELECT * FROM tb_cities WHERE id = #{Id}")
    Cities selectone(Serializable id);

}