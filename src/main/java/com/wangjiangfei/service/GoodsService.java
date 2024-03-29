package com.wangjiangfei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangjiangfei.domain.ServiceVO;
import com.wangjiangfei.entity.Goods;

import java.util.List;
import java.util.Map;

/**
 * @author wangjiangfei
 * @date 2019/7/27 17:22
 * @description
 */
public interface GoodsService extends IService<Goods> {

    Map<String, Object> list(Integer page, Integer rows, String goodsName, Integer goodsTypeId);

    Map<String, Object> listInventory(Integer page, Integer rows, String codeOrName, Integer goodsTypeId);

    ServiceVO getCode();

    boolean save(Goods goods);

    ServiceVO delete(Integer goodsId);

    Map<String, Object> getNoInventoryQuantity(Integer page, Integer rows, String nameOrCode);

    ServiceVO deleteStock(Integer goodsId);

    Map<String, Object> getHasInventoryQuantity(Integer page, Integer rows, String nameOrCode);

    ServiceVO saveStock(Integer goodsId, Integer inventoryQuantity, double purchasingPrice);

    Map<String, Object> listAlarm();

    Goods findGoods(String goodsCode, String goodsColour);

    void importSave(Goods goods);

    List<Goods> queryGoodsList();
}
