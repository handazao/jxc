package com.wangjiangfei.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangjiangfei.domain.ErrorCode;
import com.wangjiangfei.domain.ServiceVO;
import com.wangjiangfei.domain.SuccessCode;
import com.wangjiangfei.entity.TakeStock;
import com.wangjiangfei.entity.TakeStockList;
import com.wangjiangfei.service.TakeStockListService;
import com.wangjiangfei.service.TakeStockService;
import com.wangjiangfei.util.EasyPoiUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangjiangfei
 * @date 2019/7/26 10:03
 * @description 库存盘点信息Controller
 */
@RestController
@RequestMapping("/takeStock")
public class TakeStockController {

    @Autowired
    private TakeStockService takeStockService;
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
    public Map<String, Object> list(Integer page, Integer rows, TakeStock takeStock) {
        return takeStockService.list(page, rows, takeStock);
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
    public ServiceVO save(TakeStock takeStock) {
        takeStockService.save(takeStock);
        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESS);
    }

    /**
     * 删除库存盘点信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @RequiresPermissions(value = "库存盘点")
    public ServiceVO delete(Integer id) {
        TakeStock takeStock = takeStockService.getById(id);
        takeStock.setIsDeleted(1);
        takeStockService.updateById(takeStock);
        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESS);
    }

    /**
     * 库存盘点导入
     *
     * @param file
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping("/importTakeStock")
    @RequiresPermissions(value = "库存盘点")
    public ServiceVO importTakeStock(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws Exception {
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
        List<String> purchaseLists = takeStockService.importTakeStock(stringListMap);
        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESS, purchaseLists);
    }

    /**
     * 库存盘点导入
     *
     * @param response
     * @param takeStock
     * @throws Exception
     */
    @RequestMapping("/exportTakeStock")
    @RequiresPermissions(value = "库存盘点")
    public void exportTakeStock(HttpServletResponse response, TakeStock takeStock) throws Exception {
        TemplateExportParams params = new TemplateExportParams("src/main/resources/static/pandian.xlsx");
        Map<String, Object> map = new HashMap<String, Object>();

        takeStock = takeStockService.getById(takeStock.getId());

        map.put("date", takeStock.getInventoryTime());
        map.put("number", takeStock.getNumber());

        QueryWrapper<TakeStockList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("take_stock_id", takeStock.getId());
        queryWrapper.eq("is_deleted", 0);
        List<TakeStockList> takeStockListList = takeStockListService.list(queryWrapper);
        map.put("maplist", takeStockListList);

        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        try {
            String fileName = URLEncoder.encode("盘点-" + takeStock.getNumber() + ".xlsx", "UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");

            ServletOutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
