package com.wangjiangfei.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.wangjiangfei.dao.GoodsDao;
import com.wangjiangfei.dao.GoodsTypeDao;
import com.wangjiangfei.dao.PurchaseListGoodsDao;
import com.wangjiangfei.dao.UserDao;
import com.wangjiangfei.domain.ServiceVO;
import com.wangjiangfei.domain.SuccessCode;
import com.wangjiangfei.entity.*;
import com.wangjiangfei.service.GoodsService;
import com.wangjiangfei.service.LogService;
import com.wangjiangfei.service.PurchaseListGoodsService;
import com.wangjiangfei.service.SupplierService;
import com.wangjiangfei.util.BigDecimalUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangjiangfei
 * @date 2019/8/2 9:15
 * @description
 */
@Service
public class PurchaseListGoodsServiceImpl implements PurchaseListGoodsService {

    @Autowired
    private LogService logService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PurchaseListGoodsDao purchaseListGoodsDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsTypeDao goodsTypeDao;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private GoodsService goodsService;


    @Override
    public ServiceVO save(PurchaseList purchaseList, String purchaseListGoodsStr) {

        // 使用谷歌Gson将JSON字符串数组转换成具体的集合
        Gson gson = new Gson();

        List<PurchaseListGoods> purchaseListGoodsList = gson.fromJson(purchaseListGoodsStr, new TypeToken<List<PurchaseListGoods>>() {
        }.getType());

        // 设置当前操作用户
        User currentUser = userDao.findUserByName((String) SecurityUtils.getSubject().getPrincipal());

        purchaseList.setUserId(currentUser.getUserId());

        // 保存进货清单
        purchaseListGoodsDao.savePurchaseList(purchaseList);

        // 保存进货商品列表
        for (PurchaseListGoods p : purchaseListGoodsList) {
            p.setPurchaseListId(purchaseList.getPurchaseListId());
            p.setGoodsTypeId(goodsDao.findByGoodsId(p.getGoodsId()).getGoodsTypeId());
            purchaseListGoodsDao.savePurchaseListGoods(p);

            // 修改商品上一次进货价，进货均价，库存，状态
            Goods goods = goodsDao.findByGoodsId(p.getGoodsId());

            goods.setLastPurchasingPrice(p.getPrice());

            goods.setInventoryQuantity(goods.getInventoryQuantity() + p.getGoodsNum());

            goods.setPurchasingPrice(BigDecimalUtil.keepTwoDecimalPlaces((goods.getPurchasingPrice() + p.getPrice()) / 2));

            goods.setState(2);

            goodsDao.updateGoods(goods);
        }

        // 保存日志
        logService.save(new Log(Log.INSERT_ACTION, "新增进货单：" + purchaseList.getPurchaseNumber()));

        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESS);
    }

    @Override
    public Map<String, Object> list(String purchaseNumber, Integer supplierId, Integer state, String sTime, String eTime) {
        Map<String, Object> result = new HashMap<>();


        List<PurchaseList> purchaseListList = purchaseListGoodsDao.getPurchaselist(purchaseNumber, supplierId, state, sTime, eTime);

        logService.save(new Log(Log.SELECT_ACTION, "进货单据查询"));

        result.put("rows", purchaseListList);

        return result;
    }

    @Override
    public Map<String, Object> goodsList(Integer purchaseListId) {
        Map<String, Object> map = new HashMap<>();

        List<PurchaseListGoods> purchaseListGoodsList = purchaseListGoodsDao.getPurchaseListGoodsByPurchaseListId(purchaseListId);

        logService.save(new Log(Log.SELECT_ACTION, "进货单商品信息查询"));

        map.put("rows", purchaseListGoodsList);

        return map;
    }

    @Override
    public ServiceVO delete(Integer purchaseListId) {

        logService.save(new Log(Log.DELETE_ACTION, "删除进货单：" + purchaseListGoodsDao.getPurchaseListById(purchaseListId).getPurchaseNumber()));

        purchaseListGoodsDao.deletePurchaseListGoodsByPurchaseListId(purchaseListId);

        purchaseListGoodsDao.deletePurchaseListById(purchaseListId);

        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESS);
    }

    @Override
    public ServiceVO updateState(Integer purchaseListId) {
        purchaseListGoodsDao.updateState(purchaseListId);

        logService.save(new Log(Log.DELETE_ACTION, "支付结算进货单：" + purchaseListGoodsDao.getPurchaseListById(purchaseListId).getPurchaseNumber()));

        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESS);
    }

    @Override
    public String count(String sTime, String eTime, Integer goodsTypeId, String codeOrName) {

        JsonArray result = new JsonArray();

        try {

            List<PurchaseList> purchaseListList = purchaseListGoodsDao.getPurchaselist(null, null, null, sTime, eTime);

            for (PurchaseList pl : purchaseListList) {

                List<PurchaseListGoods> purchaseListGoodsList = purchaseListGoodsDao
                        .getPurchaseListGoods(pl.getPurchaseListId(), goodsTypeId, codeOrName);

                for (PurchaseListGoods pg : purchaseListGoodsList) {

                    JsonObject obj = new JsonObject();

                    obj.addProperty("number", pl.getPurchaseNumber());

                    obj.addProperty("date", pl.getPurchaseDate());

                    obj.addProperty("supplierName", pl.getSupplierName());

                    obj.addProperty("code", pg.getGoodsCode());

                    obj.addProperty("name", pg.getGoodsName());

                    obj.addProperty("model", pg.getGoodsModel());

                    obj.addProperty("goodsType", goodsTypeDao.getGoodsTypeById(pg.getGoodsTypeId()).getGoodsTypeName());

                    obj.addProperty("unit", pg.getGoodsUnit());

                    obj.addProperty("price", pg.getPrice());

                    obj.addProperty("num", pg.getGoodsNum());

                    obj.addProperty("total", pg.getTotal());

                    result.add(obj);

                }
            }

            logService.save(new Log(Log.SELECT_ACTION, "进货商品统计查询"));

        } catch (Exception e) {

            e.printStackTrace();

        }

        return result.toString();
    }

    @Override
    public void savePurchaseListGoods(PurchaseListGoods purchaseListGoods) {
        purchaseListGoodsDao.savePurchaseListGoods(purchaseListGoods);
    }

    @Override
    public void savePurchaseList(PurchaseList purchaseList) {
        if (purchaseList.getPurchaseListId() == null) {
            purchaseListGoodsDao.savePurchaseList(purchaseList);
        } else {
            purchaseListGoodsDao.updatePurchaseList(purchaseList);
        }
    }

    @Override
    public void updatePurchaseListId(Integer purchaseListId, String purchaseNumber) {
        purchaseListGoodsDao.updatePurchaseListId(purchaseListId, purchaseNumber);
    }

    @Override
    public PurchaseList getByPurchaseNumber(String purchaseNumber) {
        return purchaseListGoodsDao.getByPurchaseNumber(purchaseNumber);
    }

    @Override
    public void deletePurchaseListGoods(Integer purchaseListId) {
        purchaseListGoodsDao.deletePurchaseListGoodsByPurchaseListId(purchaseListId);
    }

    @Override
    public void deletePurchaseList(Integer purchaseListId) {
        purchaseListGoodsDao.deletePurchaseListById(purchaseListId);
    }

    @Transactional
    @Override
    public void importPurchase(Map<String, List<String[]>> stringListMap) {
        for (Map.Entry<String, List<String[]>> entry : stringListMap.entrySet()) {
            String sheetName = entry.getKey();
            List<String[]> rows = entry.getValue();

            Supplier existSupplier = supplierService.existSupplier(sheetName);
            if (existSupplier == null || existSupplier.getSupplierId() == null) {
                existSupplier = new Supplier();
                existSupplier.setSupplierName(sheetName);
                supplierService.save(existSupplier);
            }

            Map<String, PurchaseList> purchaseMap = new HashMap<>();
            for (String[] row : rows) {
                // 保存进货清单
                double goodsPares = Integer.valueOf(row[6]) * Double.parseDouble(row[7]);
                PurchaseList purchaseList = new PurchaseList();
                if (purchaseMap.containsKey(row[0])) {
                    purchaseList = purchaseMap.get(row[0]);
                    purchaseList.setAmountPaid(goodsPares + purchaseList.getAmountPaid());
                    purchaseList.setAmountPayable(goodsPares + purchaseList.getAmountPayable());
                } else {
                    try {
                        SimpleDateFormat formatIn = new SimpleDateFormat("yyyy.M.d");
                        SimpleDateFormat formatOut = new SimpleDateFormat("yyyy-MM-dd");
                        String dateString = row[0];
                        Date date = formatIn.parse(dateString);
                        String purchaseDate = formatOut.format(date);
                        Long timeDate = date.getTime();
                        int timestamps = (int) (timeDate / 1000);
                        purchaseList.setPurchaseNumber(String.valueOf(timestamps));
                        purchaseList.setPurchaseDate(purchaseDate);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    purchaseList.setAmountPaid(goodsPares);
                    purchaseList.setAmountPayable(goodsPares);
                    purchaseList.setSupplierId(existSupplier.getSupplierId());
                    purchaseList.setUserId(1);
                }

                purchaseMap.put(row[0], purchaseList);

                //保存商品
                Goods goods = goodsService.findGoods(row[3], row[4]);
                if (goods != null && goods.getGoodsId() != null) {
                    PurchaseList purchase = purchaseListGoodsDao.getByPurchaseNumber(purchaseList.getPurchaseNumber());
                    if (purchase != null && purchase.getPurchaseListId() != null) {
                        Integer purchaseListId = purchase.getPurchaseListId();
                        Integer goodsId = goods.getGoodsId();
                        PurchaseListGoods purchaseListGoods = purchaseListGoodsDao.queryPurchaseListGoods(purchaseListId, goodsId);
                        if (purchaseListGoods != null && purchaseListGoods.getPurchaseListGoodsId() != null) {
                            goods.setLastPurchasingPrice(goods.getPurchasingPrice());
                            goods.setInventoryQuantity(goods.getInventoryQuantity() + Integer.valueOf(row[6]) - purchaseListGoods.getGoodsNum());
                        } else {
                            goods.setLastPurchasingPrice(goods.getPurchasingPrice());
                            goods.setInventoryQuantity(goods.getInventoryQuantity() + Integer.valueOf(row[6]));
                        }
                    } else {
                        goods.setLastPurchasingPrice(goods.getPurchasingPrice());
                        goods.setInventoryQuantity(goods.getInventoryQuantity() + Integer.valueOf(row[6]));
                    }
                } else {
                    goods = new Goods();
                    goods.setPurchasingPrice(Double.parseDouble(row[7]));
                    goods.setLastPurchasingPrice(Double.parseDouble(row[7]));
                    goods.setInventoryQuantity(Integer.valueOf(row[6]));
                }
                goods.setGoodsCode(row[3]);
                goods.setGoodsName(row[2]);
                goods.setMinNum(1);
                goods.setGoodsModel(row[3]);
                goods.setGoodsProducer(sheetName);
                goods.setState(2);
                goods.setGoodsUnit("1");
                goods.setGoodsTypeId(2);
                goods.setSeason(row[1]);
                goods.setGoodsColour(row[4]);
                goods.setGoodsSize(row[5]);
                goods.setInPurchase(row[8]);
                goods.setRetention(row[9]);
                goods.setRemarks(row[10]);
                goodsService.importSave(goods);


                // 保存进货商品清单
                PurchaseListGoods purchaseListGoods = new PurchaseListGoods();
                purchaseListGoods.setGoodsId(goods.getGoodsId());
                purchaseListGoods.setGoodsCode(goods.getGoodsCode());
                purchaseListGoods.setGoodsName(goods.getGoodsName());
                purchaseListGoods.setGoodsModel(goods.getGoodsModel());
                purchaseListGoods.setGoodsUnit(goods.getGoodsUnit());
                purchaseListGoods.setGoodsNum(Integer.valueOf(row[6]));
                purchaseListGoods.setPrice(Double.parseDouble(row[7]));
                purchaseListGoods.setTotal(goodsPares);
                purchaseListGoods.setPurchaseListId(Integer.valueOf(purchaseList.getPurchaseNumber()));
                purchaseListGoods.setGoodsTypeId(2);

                purchaseListGoodsDao.savePurchaseListGoods(purchaseListGoods);

            }

            // 保存进货清单
            for (Map.Entry<String, PurchaseList> purchaseListEntry : purchaseMap.entrySet()) {
                PurchaseList purchaseList = purchaseListEntry.getValue();
                PurchaseList purchase = purchaseListGoodsDao.getByPurchaseNumber(purchaseList.getPurchaseNumber());
                if (purchase != null && purchase.getPurchaseListId() != null) {
                    this.deletePurchaseList(purchase.getPurchaseListId());
                    this.deletePurchaseListGoods(purchase.getPurchaseListId());
                }
                purchaseListGoodsDao.savePurchaseList(purchaseList);
                purchaseListGoodsDao.updatePurchaseListId(purchaseList.getPurchaseListId(), purchaseList.getPurchaseNumber());
            }

        }
    }
}
