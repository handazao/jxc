package com.wangjiangfei.controller;

import com.wangjiangfei.domain.ErrorCode;
import com.wangjiangfei.domain.ServiceVO;
import com.wangjiangfei.domain.SuccessCode;
import com.wangjiangfei.entity.PurchaseList;
import com.wangjiangfei.service.PurchaseListGoodsService;
import com.wangjiangfei.util.EasyPoiUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author wangjiangfei
 * @date 2019/8/2 9:10
 * @description 进货商品Controller层
 */
@RestController
@RequestMapping("/purchaseListGoods")
public class PurchaseListGoodsController {

    @Autowired
    private PurchaseListGoodsService purchaseListGoodsService;


    /**
     * 保存进货单信息
     *
     * @param purchaseList         进货单信息实体
     * @param purchaseListGoodsStr 进货商品信息JSON字符串
     * @return
     */
    @RequestMapping("/save")
    @RequiresPermissions(value = "进货入库")
    public ServiceVO save(PurchaseList purchaseList, String purchaseListGoodsStr) {
        return purchaseListGoodsService.save(purchaseList, purchaseListGoodsStr);
    }

    /**
     * 查询进货单
     *
     * @param purchaseNumber 单号
     * @param supplierId     供应商ID
     * @param state          付款状态
     * @param sTime          开始时间
     * @param eTime          结束时间
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions(value = {"进货单据查询", "供应商统计"}, logical = Logical.OR)
    public Map<String, Object> list(String purchaseNumber, Integer supplierId, Integer state, String sTime,
                                    String eTime, String purchaseNumbers, Integer type) {
        if (StringUtils.isNotBlank(purchaseNumbers)) {
            String[] purchaseNumbersArray = purchaseNumbers.split(",");
            List<String> purchaseNumberList = Arrays.asList(purchaseNumbersArray);
            return purchaseListGoodsService.list(purchaseNumber, supplierId, state, sTime, eTime, purchaseNumberList, type);
        }
        return purchaseListGoodsService.list(purchaseNumber, supplierId, state, sTime, eTime, null, null);
    }

    /**
     * 查询进货单商品信息
     *
     * @param purchaseListId 进货单ID
     * @return
     */
    @RequestMapping("/goodsList")
    @RequiresPermissions(value = {"进货单据查询", "供应商统计"}, logical = Logical.OR)
    public Map<String, Object> goodsList(Integer purchaseListId) {
        return purchaseListGoodsService.goodsList(purchaseListId);
    }

    /**
     * 删除进货单及商品信息
     *
     * @param purchaseListId 进货单ID
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions(value = "进货单据查询")
    public ServiceVO delete(Integer purchaseListId) {
        return purchaseListGoodsService.delete(purchaseListId);
    }

    /**
     * 修改进货单付款状态
     *
     * @param purchaseListId 进货单ID
     * @return
     */
    @RequestMapping("/updateState")
    @RequiresPermissions(value = "供应商统计")
    public ServiceVO updateState(Integer purchaseListId) {
        return purchaseListGoodsService.updateState(purchaseListId);
    }

    /**
     * 进货商品统计
     *
     * @param sTime       开始时间
     * @param eTime       结束时间
     * @param goodsTypeId 商品类别ID
     * @param codeOrName  编号或商品名称
     * @return
     */
    @RequestMapping("/count")
    @RequiresPermissions(value = "商品采购统计")
    public String count(String sTime, String eTime, Integer goodsTypeId, String codeOrName) {
        return purchaseListGoodsService.count(sTime, eTime, goodsTypeId, codeOrName);
    }

    /**
     * 进货入库导入
     *
     * @param file
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping("/importPurchase")
    @RequiresPermissions(value = "批量入库")
    public ServiceVO importPurchase(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws Exception {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return new ServiceVO<>(ErrorCode.PARA_TYPE_ERROR_CODE, "请上传文件");
        }
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx") && !fileName.endsWith(".xlsm")) {
            redirectAttributes.addFlashAttribute("message", "Please select a correct file to upload");
            return new ServiceVO<>(ErrorCode.PARA_TYPE_ERROR_CODE, "文件格式错误");
        }
        Map<String, List<String[]>> stringListMap = EasyPoiUtil.readExcel(file);
        List<String> purchaseLists = purchaseListGoodsService.importPurchase(stringListMap);
        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESS, purchaseLists);
    }

    /**
     * 批量入库
     *
     * @param response
     * @param takeStock
     * @throws Exception
     */
    @RequestMapping("/export")
    @RequiresPermissions(value = "批量入库")
    public void download(HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File("src/main/resources/static/template.xlsx");
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=template.xlsx");
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
