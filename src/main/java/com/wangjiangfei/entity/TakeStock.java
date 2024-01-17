package com.wangjiangfei.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date inventoryTime;
    private Integer inventoryVariance;
    private Integer status;
    private Integer isDeleted;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

}
