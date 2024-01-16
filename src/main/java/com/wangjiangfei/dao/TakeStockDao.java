package com.wangjiangfei.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangjiangfei.entity.TakeStock;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangjiangfei
 * @date 2019/7/27 17:21
 * @description
 */
public interface TakeStockDao extends BaseMapper<TakeStock> {

    List<TakeStock> getTakeStockList(@Param("offSet") Integer offSet, @Param("pageRow") Integer pageRow, @Param("takeStock") TakeStock takeStock);

    Integer getTakeStockCount(@Param("takeStock") TakeStock takeStock);
}
