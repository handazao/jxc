package com.wangjiangfei.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_take_stock")
public class TakeStock {

    private String id;
    private String number;
    private Integer inventoryQuantity;
    private double purchasePrice;
    private Integer countQuantity;
    private Integer surplusQuantity;
    private double surplusAmount;
    private Date inventoryTime;
    private Integer inventoryVariance;
    private Integer status;
    private Integer isDeleted;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

}
