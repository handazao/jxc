package com.wangjiangfei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("t_take_stock_list")
public class TakeStockList {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String takeStockId;
    private Integer goodsId;
    private String goodsCode;
    private String goodsName;
    private String season;
    private String goodsColour;
    private String goodsSize;
    private Integer inventoryQuantity;
    private Integer countQuantity;
    private double purchasePrice;
    private Integer surplusQuantity;
    private double surplusAmount;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date inventoryTime;
    private Integer inventoryVariance;
    private Integer isDeleted;
    private String remarks;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

}
