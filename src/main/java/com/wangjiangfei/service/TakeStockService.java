package com.wangjiangfei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangjiangfei.entity.TakeStock;

import java.util.List;
import java.util.Map;

/**
 * @author wangjiangfei
 * @date 2019/7/27 17:22
 * @description
 */
public interface TakeStockService extends IService<TakeStock> {

    Map<String, Object> list(Integer page, Integer rows, TakeStock takeStock);

    boolean save(TakeStock takeStock);

    List<String> importTakeStock(Map<String, List<String[]>> stringListMap, String id);
}
