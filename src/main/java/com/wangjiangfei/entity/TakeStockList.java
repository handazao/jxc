package com.wangjiangfei.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_take_stock_list")
public class TakeStockList {

    private Integer id;
    private String takeStockId;
    private Integer goodsId;
    private String goodsCode;
    private String goodsName;
    private Integer inventoryQuantity;
    private Integer countQuantity;
    private double purchasePrice;
    private Integer surplusQuantity;
    private double surplusAmount;
    private Date inventoryTime;
    private Integer inventoryVariance;
    private Integer isDeleted;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

}
