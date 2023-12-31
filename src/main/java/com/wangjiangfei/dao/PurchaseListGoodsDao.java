package com.wangjiangfei.dao;

import com.wangjiangfei.entity.PurchaseList;
import com.wangjiangfei.entity.PurchaseListGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangjiangfei
 * @date 2019/8/2 9:33
 * @description
 */
public interface PurchaseListGoodsDao {

    Integer savePurchaseList(PurchaseList purchaseList);

    Integer savePurchaseListGoods(PurchaseListGoods p);

    List<PurchaseList> getPurchaselist(@Param("purchaseNumber") String purchaseNumber,
                                       @Param("supplierId") Integer supplierId,
                                       @Param("state") Integer state,
                                       @Param("sTime") String sTime,
                                       @Param("eTime") String eTime,
                                       @Param("purchaseNumberList") List<String> purchaseNumberList,
                                       @Param("type") Integer type);

    List<PurchaseListGoods> getPurchaseListGoodsByPurchaseListId(Integer purchaseListId);

    PurchaseList getPurchaseListById(Integer purchaseListId);

    Integer deletePurchaseListById(Integer purchaseListId);

    Integer deletePurchaseListGoodsByPurchaseListId(Integer purchaseListId);

    Integer updateState(Integer purchaseListId);

    List<PurchaseListGoods> getPurchaseListGoods(@Param("purchaseListId") Integer purchaseListId,
                                                 @Param("goodsTypeId") Integer goodsTypeId,
                                                 @Param("codeOrName") String codeOrName);

    void updatePurchaseListId(@Param("purchaseListId") Integer purchaseListId, @Param("purchaseNumber") String purchaseNumber);

    PurchaseList getByPurchaseNumber(@Param("purchaseNumber") String purchaseNumber);

    Integer updatePurchaseList(PurchaseList purchaseList);

    PurchaseListGoods queryPurchaseListGoods(@Param("purchaseListId") Integer purchaseListId, @Param("goodsId") Integer goodsId);
}
