package com.wangjiangfei.dao;

import com.wangjiangfei.entity.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangjiangfei
 * @date 2019/7/27 17:21
 * @description
 */
public interface GoodsDao {

    List<Goods> getGoodsList(@Param("offSet") Integer offSet,@Param("pageRow") Integer pageRow,@Param("goodsName") String goodsName,@Param("goodsTypeId") Integer goodsTypeId);

    Integer getGoodsCount(@Param("goodsName") String goodsName,@Param("goodsTypeId") Integer goodsTypeId);

    String getMaxCode();

    Integer saveGoods(Goods goods);

    Integer updateGoods(Goods goods);

    Integer deleteGoods(Integer goodsId);

    Goods findByGoodsId(Integer goodsId);

    List<Goods> getNoInventoryQuantity(@Param("offSet") Integer offSet,@Param("pageRow") Integer pageRow,@Param("nameOrCode") String nameOrCode);

    Integer getNoInventoryQuantityCount(@Param("nameOrCode") String nameOrCode);

    List<Goods> getHasInventoryQuantity(@Param("offSet") Integer offSet,@Param("pageRow") Integer pageRow,@Param("nameOrCode") String nameOrCode);

    Integer getHasInventoryQuantityCount(@Param("nameOrCode")String nameOrCode);

    List<Goods> getGoodsInventoryList(@Param("offSet") Integer offSet,@Param("pageRow") Integer pageRow, @Param("codeOrName") String codeOrName,@Param("goodsTypeId") Integer goodsTypeId);

    Integer getGoodsInventoryCount(@Param("codeOrName") String codeOrName,@Param("goodsTypeId") Integer goodsTypeId);

    // 查询当前库存小于最小库存的商品
    List<Goods> getGoodsAlarm();

    /**
     * 根据商品编码和颜色查询商品
     *
     * @param goodsCode
     * @param goodsColour
     * @return
     */
    Goods findGoods(@Param("goodsCode") String goodsCode, @Param("goodsColour") String goodsColour);

    List<Goods> queryGoodsList();
}
