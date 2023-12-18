package com.wangjiangfei.controller;

import com.wangjiangfei.domain.ErrorCode;
import com.wangjiangfei.domain.ServiceVO;
import com.wangjiangfei.entity.Goods;
import com.wangjiangfei.entity.PurchaseList;
import com.wangjiangfei.entity.Supplier;
import com.wangjiangfei.service.GoodsService;
import com.wangjiangfei.service.PurchaseListGoodsService;
import com.wangjiangfei.service.SupplierService;
import com.wangjiangfei.util.EasyPoiUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private GoodsService goodsService;


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
                                    String eTime) {
        return purchaseListGoodsService.list(purchaseNumber, supplierId, state, sTime, eTime);
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

        for (Map.Entry<String, List<String[]>> entry : stringListMap.entrySet()) {
            String sheetName = entry.getKey();
            List<String[]> rows = entry.getValue();

            boolean isExistSupplier = supplierService.existSupplier(sheetName);
            if (!isExistSupplier) {
                Supplier supplier = new Supplier();
                supplier.setSupplierName(sheetName);
                supplierService.save(supplier);
            }


            for (String[] row : rows) {
                Goods goods = new Goods();
                goods.setGoodsCode(row[3]);
                goods.setGoodsName(row[2]);
                goods.setInventoryQuantity(Integer.valueOf(row[6]));
                goods.setMinNum(1);
                goods.setGoodsModel(row[3]);
                goods.setGoodsProducer(sheetName);
                goods.setPurchasingPrice(Double.parseDouble(row[7]));
                goods.setLastPurchasingPrice(Double.parseDouble(row[7]));
                goods.setState(2);
                goods.setGoodsUnit("1");
                goods.setGoodsTypeId(2);
                goods.setSeason(row[1]);
                goods.setGoodsColour(row[4]);
                goods.setGoodsSize(row[5]);
                goods.setInPurchase(row[8]);
                goods.setRetention(row[9]);
                goods.setRemarks(row[10]);
                //goodsService.save(goods);


                //purchaseListGoodsService.save(purchaseList, purchaseListGoodsStr);
            }
        }

        return null;
    }
}
