package com.wangjiangfei.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangjiangfei.dao.TakeStockListDao;
import com.wangjiangfei.entity.Log;
import com.wangjiangfei.entity.TakeStockList;
import com.wangjiangfei.service.LogService;
import com.wangjiangfei.service.TakeStockListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class TakeStockListServiceImpl extends ServiceImpl<TakeStockListDao, TakeStockList> implements TakeStockListService {

    @Autowired
    private LogService logService;
    @Autowired
    private TakeStockListDao takeStockListDao;

    @Override
    public void deleteByTakeStockId(String id) {
        QueryWrapper<TakeStockList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("take_stock_id", id);

        TakeStockList takeStockList = new TakeStockList();
        takeStockList.setIsDeleted(1);
        baseMapper.update(takeStockList, queryWrapper);
    }

    @Override
    public Map<String, Object> list(Integer page, Integer rows, TakeStockList takeStockList) {
        Map<String, Object> map = new HashMap<>();

        page = page == 0 ? 1 : page;
        int offSet = (page - 1) * rows;

        // 查询类别ID为当前ID或父ID为当前类别ID的商品
        List<TakeStockList> takeStockLists = takeStockListDao.getTakeStockLists(offSet, rows, takeStockList);

        map.put("rows", takeStockLists);

        map.put("total", takeStockListDao.getTakeStockListCount(takeStockList));

        logService.save(new Log(Log.SELECT_ACTION, "分页查询商品信息"));

        return map;
    }
}
