package com.wangjiangfei.service;

import com.wangjiangfei.domain.ServiceVO;
import com.wangjiangfei.entity.PurchaseList;
import com.wangjiangfei.entity.PurchaseListGoods;

import java.util.List;
import java.util.Map;

/**
 * @author wangjiangfei
 * @date 2019/8/2 9:15
 * @description
 */
public interface PurchaseListGoodsService {

    ServiceVO save(PurchaseList purchaseList, String purchaseListGoodsStr);

    Map<String, Object> list(String purchaseNumber, Integer supplierId, Integer state, String sTime, String eTime, List<String> purchaseNumberList, Integer type);

    Map<String, Object> goodsList(Integer purchaseListId);

    ServiceVO delete(Integer purchaseListId);

    ServiceVO updateState(Integer purchaseListId);

    String count(String sTime, String eTime, Integer goodsTypeId, String codeOrName);

    void savePurchaseListGoods(PurchaseListGoods purchaseListGoods);

    void savePurchaseList(PurchaseList purchaseList);

    void updatePurchaseListId(Integer purchaseListId, String purchaseNumber);

    PurchaseList getByPurchaseNumber(String purchaseNumber);

    void deletePurchaseListGoods(Integer purchaseListId);

    void deletePurchaseList(Integer purchaseListId);

    List<String> importPurchase(Map<String, List<String[]>> stringListMap);
}
