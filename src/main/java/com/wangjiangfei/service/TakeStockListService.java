package com.wangjiangfei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangjiangfei.entity.TakeStockList;

import java.util.Map;

public interface TakeStockListService extends IService<TakeStockList> {


    void deleteByTakeStockId(String id);

    Map<String, Object> list(Integer page, Integer rows, TakeStockList takeStockList);
}
