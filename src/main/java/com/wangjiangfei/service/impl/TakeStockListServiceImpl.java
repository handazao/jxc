package com.wangjiangfei.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.wangjiangfei.dao.TakeStockListDao;
import com.wangjiangfei.entity.*;
import com.wangjiangfei.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Slf4j
public class TakeStockListServiceImpl extends ServiceImpl<TakeStockListDao, TakeStockList> implements TakeStockListService {

    @Autowired
    private LogService logService;
    @Autowired
    private TakeStockListDao takeStockListDao;
    @Autowired
    private TakeStockService takeStockService;
    @Autowired
    private OverflowListGoodsService overflowListGoodsService;
    @Autowired
    private DamageListGoodsService damageListGoodsService;

    @Override
    public void deleteByTakeStockId(String id) {
        QueryWrapper<TakeStockList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("take_stock_id", id);

        TakeStockList takeStockList = new TakeStockList();
        takeStockList.setIsDeleted(1);
        this.baseMapper.update(takeStockList, queryWrapper);
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

    @Override
    public void updateTakeStockList(TakeStockList takeStockList) {
        QueryWrapper<TakeStockList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", takeStockList.getId());

        TakeStockList takeStockList1 = this.baseMapper.selectById(takeStockList.getId());
        int cq = takeStockList.getCountQuantity() - takeStockList1.getCountQuantity();
        int surplusQuantity = takeStockList1.getSurplusQuantity();
        BigDecimal surplusAmount = BigDecimal.valueOf(takeStockList1.getSurplusAmount());

        takeStockList1.setCountQuantity(takeStockList.getCountQuantity());
        takeStockList1.setRemarks(takeStockList.getRemarks());
        takeStockList1.setSurplusQuantity(takeStockList.getCountQuantity() - takeStockList1.getInventoryQuantity());
        takeStockList1.setSurplusAmount(takeStockList1.getPurchasePrice() * takeStockList.getCountQuantity());

        int sq = takeStockList1.getSurplusQuantity() - surplusQuantity;
        BigDecimal sa = new BigDecimal(takeStockList1.getSurplusAmount()).subtract(surplusAmount);
        if (takeStockList1.getSurplusQuantity() > 0) {
            takeStockList1.setInventoryVariance(1);
            //盘盈商品
            String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
            OverflowList overflowList = new OverflowList();
            overflowList.setOverflowNumber("PY" + today);
            overflowList.setOverflowDate(today);
            overflowList.setRemarks("盘盈商品");

            OverflowListGoods overflowListGoods = new OverflowListGoods();
            overflowListGoods.setGoodsId(takeStockList.getGoodsId());
            overflowListGoods.setGoodsCode(takeStockList.getGoodsCode());
            overflowListGoods.setGoodsName(takeStockList.getGoodsName());
            overflowListGoods.setGoodsColour(takeStockList.getGoodsColour());
            overflowListGoods.setSeason(takeStockList.getSeason());
            overflowListGoods.setGoodsSize(takeStockList.getGoodsSize());
            //overflowListGoods.setGoodsUnit(goods.getGoodsUnit());
            overflowListGoods.setGoodsNum(takeStockList.getSurplusQuantity());
            overflowListGoods.setPrice(takeStockList.getPurchasePrice());
            overflowListGoods.setTotal(takeStockList.getPurchasePrice() * takeStockList.getSurplusQuantity());
            overflowListGoods.setOverflowListId(overflowList.getOverflowListId());
            //overflowListGoods.setGoodsTypeId(goods.getGoodsTypeId());
            List<OverflowListGoods> overflowListGoodsList = new ArrayList<>();
            overflowListGoodsList.add(overflowListGoods);
            Gson gson = new Gson();

            overflowListGoodsService.save(overflowList, gson.toJson(overflowListGoodsList));
        } else if (takeStockList1.getSurplusQuantity() < 0) {
            takeStockList1.setInventoryVariance(-1);
            //盘亏商品
            String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
            DamageList damageList = new DamageList();
            damageList.setDamageNumber("PK" + today);
            damageList.setDamageDate(today);
            damageList.setRemarks("盘亏商品");

            DamageListGoods damageListGoods = new DamageListGoods();
            damageListGoods.setGoodsId(takeStockList.getGoodsId());
            damageListGoods.setGoodsCode(takeStockList.getGoodsCode());
            damageListGoods.setGoodsName(takeStockList.getGoodsName());
            damageListGoods.setGoodsColour(takeStockList.getGoodsColour());
            damageListGoods.setSeason(takeStockList.getSeason());
            damageListGoods.setGoodsSize(takeStockList.getGoodsSize());
            //damageListGoods.setGoodsUnit(goods.getGoodsUnit());
            damageListGoods.setGoodsNum(takeStockList.getSurplusQuantity());
            damageListGoods.setPrice(takeStockList.getPurchasePrice());
            damageListGoods.setTotal(takeStockList.getPurchasePrice() * takeStockList.getSurplusQuantity());
            damageListGoods.setDamageListGoodsId(damageList.getDamageListId());
            //damageListGoods.setGoodsTypeId(goods.getGoodsTypeId());
            List<DamageListGoods> damageListGoodsList = new ArrayList<>();
            damageListGoodsList.add(damageListGoods);
            Gson gson = new Gson();

            damageListGoodsService.save(damageList, gson.toJson(damageListGoodsList));
        } else if (takeStockList1.getSurplusQuantity() == 0) {
            takeStockList1.setInventoryVariance(0);
        }
        this.baseMapper.update(takeStockList1, queryWrapper);

        TakeStock takeStock = takeStockService.getById(takeStockList1.getTakeStockId());
        takeStock.setCountQuantity(takeStock.getCountQuantity() + cq);
        takeStock.setSurplusQuantity(takeStock.getSurplusQuantity() + sq);
        BigDecimal sat = new BigDecimal(takeStock.getSurplusAmount()).add(sa);
        takeStock.setSurplusAmount(sat.doubleValue());
        if (takeStock.getSurplusQuantity() > 0) {
            takeStock.setInventoryVariance(1);
        } else if (takeStock.getSurplusQuantity() < 0) {
            takeStock.setInventoryVariance(-1);
        } else if (takeStock.getSurplusQuantity() == 0) {
            takeStock.setInventoryVariance(0);
        }
        takeStockService.updateById(takeStock);
    }
}
