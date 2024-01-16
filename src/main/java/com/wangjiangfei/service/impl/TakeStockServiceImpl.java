package com.wangjiangfei.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangjiangfei.dao.TakeStockDao;
import com.wangjiangfei.entity.*;
import com.wangjiangfei.service.GoodsService;
import com.wangjiangfei.service.LogService;
import com.wangjiangfei.service.TakeStockListService;
import com.wangjiangfei.service.TakeStockService;
import com.wangjiangfei.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service
@Slf4j
public class TakeStockServiceImpl extends ServiceImpl<TakeStockDao, TakeStock> implements TakeStockService {


    @Autowired
    private LogService logService;
    @Autowired
    private TakeStockDao takeStockDao;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private TakeStockListService takeStockListServide;

    @Override
    public Map<String, Object> list(Integer page, Integer rows, TakeStock takeStock) {
        Map<String, Object> map = new HashMap<>();

        page = page == 0 ? 1 : page;
        int offSet = (page - 1) * rows;

        // 查询类别ID为当前ID或父ID为当前类别ID的商品
        List<TakeStock> takeStockList = takeStockDao.getTakeStockList(offSet, rows, takeStock);

        map.put("rows", takeStockList);

        map.put("total", takeStockDao.getTakeStockCount(takeStock));

        logService.save(new Log(Log.SELECT_ACTION, "分页查询商品信息"));

        return map;
    }

    @Override
    public boolean save(TakeStock takeStock) {
        if (takeStock.getId() == null) {
            takeStock.setId(UUIDUtils.get32UUID());
            int inventoryQuantitySum = 0;
            BigDecimal purchasePriceSum = BigDecimal.ZERO;
            logService.save(new Log(Log.INSERT_ACTION, "添加库存盘点信息:" + takeStock.getNumber()));
            List<Goods> goodsList = goodsService.queryGoodsList();
            for (Goods goods : goodsList) {
                inventoryQuantitySum += goods.getInventoryQuantity();
                BigDecimal purchasePrice = BigDecimal.valueOf(goods.getPurchasingPrice()).multiply(BigDecimal.valueOf(goods.getInventoryQuantity()));
                purchasePriceSum = purchasePriceSum.add(purchasePrice);
                TakeStockList takeStockList = new TakeStockList();
                takeStockList.setTakeStockId(takeStock.getId());
                takeStockList.setGoodsCode(goods.getGoodsCode());
                takeStockList.setGoodsName(goods.getGoodsName());
                takeStockList.setGoodsId(goods.getGoodsId());
                takeStockList.setInventoryQuantity(goods.getInventoryQuantity());
                takeStockList.setPurchasePrice(goods.getPurchasingPrice());
                takeStockListServide.save(takeStockList);
            }
            String userName = (String) SecurityUtils.getSubject().getPrincipal();
            //当前日期
            String today = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
            takeStock.setNumber("PD" + today);
            takeStock.setInventoryQuantity(inventoryQuantitySum);
            takeStock.setPurchasePrice(purchasePriceSum.doubleValue());
            takeStock.setStatus(0);
            takeStock.setCreateBy(userName);
            takeStock.setCreateTime(new Date());
            takeStockDao.insert(takeStock);
        }
        return true;
    }

    @Override
    public List<String> importTakeStock(Map<String, List<String[]>> stringListMap) {
        List<String> purchaseLists = new ArrayList<>();
        logService.save(new Log(Log.IMPORT_ACTION, "库存盘点导入开始"));
        log.info("--------------------库存盘点导入开始------------------------");
        for (Map.Entry<String, List<String[]>> entry : stringListMap.entrySet()) {
            int inventoryQuantitySum = 0;
            BigDecimal purchasePriceSum = BigDecimal.ZERO;

            String sheetName = entry.getKey();
            List<String[]> rows = entry.getValue();
            log.info("导入名称:{}", sheetName);
            Map<String, PurchaseList> purchaseMap = new HashMap<>();
            for (String[] row : rows) {

                /*inventoryQuantitySum += goods.getInventoryQuantity();
                BigDecimal purchasePrice = BigDecimal.valueOf(goods.getPurchasingPrice()).multiply(BigDecimal.valueOf(goods.getInventoryQuantity()));
                purchasePriceSum = purchasePriceSum.add(purchasePrice);
                TakeStockList takeStockList = new TakeStockList();
                takeStockList.setTakeStockId(takeStock.getId());
                takeStockList.setGoodsCode(goods.getGoodsCode());
                takeStockList.setGoodsName(goods.getGoodsName());
                takeStockList.setGoodsId(goods.getGoodsId());
                takeStockList.setInventoryQuantity(goods.getInventoryQuantity());
                takeStockList.setPurchasePrice(goods.getPurchasingPrice());
                takeStockListServide.save(takeStockList);*/
            }
        }
        log.info("--------------------导入结束------------------------");
        return purchaseLists;
    }

}
