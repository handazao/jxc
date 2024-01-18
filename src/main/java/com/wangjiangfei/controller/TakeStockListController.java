package com.wangjiangfei.controller;

import com.wangjiangfei.domain.ServiceVO;
import com.wangjiangfei.domain.SuccessCode;
import com.wangjiangfei.entity.TakeStockList;
import com.wangjiangfei.service.TakeStockListService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author wangjiangfei
 * @date 2019/7/26 10:03
 * @description 库存盘点信息Controller
 */
@RestController
@RequestMapping("/takeStockList")
public class TakeStockListController {

    @Autowired
    private TakeStockListService takeStockListService;


    /**
     * 分页查询库存盘点信息
     *
     * @param page
     * @param rows
     * @param takeStock
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions(value = "库存盘点")
    public Map<String, Object> list(Integer page, Integer rows, TakeStockList takeStockList) {
        return takeStockListService.list(page, rows, takeStockList);
    }

    /**
     * 添加或修改库存盘点信息
     *
     * @param takeStock
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    @RequiresPermissions(value = "库存盘点")
    public ServiceVO save(TakeStockList takeStockList) {
        takeStockListService.updateTakeStockList(takeStockList);
        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESS);
    }
}
