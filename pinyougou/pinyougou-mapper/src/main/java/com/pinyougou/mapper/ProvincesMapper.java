package com.pinyougou.mapper;

import com.pinyougou.pojo.Provinces;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.io.Serializable;

/**
 * ProvincesMapper 数据访问接口
 * @date 2019-02-27 09:55:07
 * @version 1.0
 */
public interface ProvincesMapper extends Mapper<Provinces>{

    @Select("SELECT * FROM tb_provinces WHERE id = #{Id}")
    Provinces selectone(Serializable id);
}