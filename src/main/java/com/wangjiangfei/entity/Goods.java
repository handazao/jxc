package com.wangjiangfei.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_goods")
public class Goods {

  @TableId
  private Integer goodsId;
  private String goodsCode;
  private String goodsName;
  private Integer inventoryQuantity;
  private double lastPurchasingPrice;
  private Integer minNum;
  private String goodsModel;
  private String goodsProducer;
  private double purchasingPrice;
  private String remarks;
  private double sellingPrice;
  private Integer state;// 0 初始化状态 1 期初库存入仓库  2  有进货或者销售单据
  private String goodsUnit;
  private Integer goodsTypeId;

  @TableField(exist = false)
  private String goodsTypeName;
  @TableField(exist = false)
  private Integer saleTotal;// 销售总量

  private String season;
  private String goodsColour;
  private String goodsSize;
  private String inPurchase;
  private String retention;

}
