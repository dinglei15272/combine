package com.pinyougou.mapper;

import com.pinyougou.pojo.Areas;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * AreasMapper 数据访问接口
 * @date 2019-02-27 09:55:07
 * @version 1.0
 */
@org.apache.ibatis.annotations.Mapper
public interface AreasMapper extends Mapper<Areas>{

    @Select("SELECT * FROM tb_areas WHERE cityid = #{parentId}")
    List<Areas> findItemCatByParentId(Long parentId);
    @Select("SELECT * FROM tb_areas WHERE cityid = #{parentId}")
    List<Areas> BNO(Long parentId);
}