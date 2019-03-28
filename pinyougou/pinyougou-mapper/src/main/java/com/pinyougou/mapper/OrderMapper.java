package com.pinyougou.mapper;

import com.pinyougou.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * OrderMapper 数据访问接口
 * @date 2019-02-27 09:55:07
 * @version 1.0
 */
public interface OrderMapper extends Mapper<Order>{
    @Select("SELECT * FROM tb_order WHERE user_id = #{userId} limit #{page},#{rows}")
    List<Order> findAll(@Param("page")Integer page,@Param("rows")Integer rows,@Param("userId") String userId);
}