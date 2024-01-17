package com.wangjiangfei.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangjiangfei.entity.TakeStockList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangjiangfei
 * @date 2019/7/27 17:21
 * @description
 */
public interface TakeStockListDao extends BaseMapper<TakeStockList> {

    List<TakeStockList> getTakeStockLists(@Param("offSet") int offSet, @Param("rows") Integer rows, @Param("takeStockList") TakeStockList takeStockList);

    Object getTakeStockListCount(@Param("takeStockList") TakeStockList takeStockList);
}
