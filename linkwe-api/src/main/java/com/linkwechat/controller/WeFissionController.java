package com.linkwechat.controller;


import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.poi.LwExcelUtil;
import com.linkwechat.domain.fission.WeFission;
import com.linkwechat.domain.fission.vo.*;
import com.linkwechat.domain.live.WeLive;
import com.linkwechat.service.IWeFissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * 裂变相关
 */
@RestController
@RequestMapping("/new/fission")
public class WeFissionController  extends BaseController {

    @Autowired
    IWeFissionService iWeFissionService;


    /**
     * 获取裂变列表
     * @param weFission
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo<List<WeFission>> list(WeFission weFission){
        startPage();

        return getDataTable(
                iWeFissionService.findWeFissions(weFission)
        );

    }


    /**
     * 创建裂变
     * @param weFission
     * @return
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody WeFission weFission){

        iWeFissionService.save(weFission);
        return AjaxResult.success();
    }


    /**
     * 更新裂变
     * @param weFission
     * @return
     */
    @PutMapping("/update")
    public AjaxResult update(@RequestBody WeFission weFission){

        iWeFissionService.updateById(weFission);

        return AjaxResult.success();
    }


    /**
     * 获取任务信息
     * @param id
     * @return
     */
    @GetMapping("/getWeFissionDetail/{id}")
    public AjaxResult<WeFission> getWeFissionDetail(@PathVariable Long id){

        return AjaxResult.success(
                iWeFissionService.getById(id)
        );

    }



    /**
     * 通过id批量删除裂变
     *
     * @param ids id列表
     * @return 结果
     */
    @DeleteMapping(path = "/{ids}")
    public AjaxResult batchDeleteFission(@PathVariable("ids") Long[] ids) {
        iWeFissionService.removeByIds(Arrays.asList(ids));
        return AjaxResult.success();
    }


    /*****************************************
     ***************统计相关*******************
     ****************************************/

    /**
     * 裂变头部汇总统计
     * @param fissionId
     * @return
     */
    @GetMapping("/findWeFissionTab/{fissionId}")
    public AjaxResult<WeFissionTabVo> findWeFissionTab(@PathVariable Long fissionId){


        return AjaxResult.success(
                iWeFissionService.findWeFissionTab(fissionId)
        );

    }


    /**
     * 数据趋势
     * @param weFission
     * @return
     */
    @GetMapping("/findWeFissionTrend")
    public AjaxResult<List<WeFissionTrendVo>> findWeFissionTrend(WeFission weFission){

        startPage();

        return AjaxResult.success(
                iWeFissionService.findWeFissionTrend(weFission)
        );

    }

    /**
     * 数据报表
     * @param weFission
     * @return
     */
    @GetMapping("/findWeFissionDataReport")
    public TableDataInfo<List<WeFissionDataReportVo>>  findWeFissionDataReport(WeFission weFission){

        startPage();

        return getDataTable(
                iWeFissionService.findWeFissionDataReport(weFission)
        );

    }


    /**
     * 导出数据报表
     * @return
     */
    @GetMapping("/exportWeFissionDataReport")
    public void exportWeFissionDataReport(){

        LwExcelUtil.exprotForWeb(
                ServletUtils.getResponse(), WeFissionDataReportVo.class, iWeFissionService.findWeFissionDataReport(new WeFission()),"数据报表"
        );

    }





    /**
     * 裂变明细
     * @param customerName
     * @param weUserId
     * @return
     */
    @GetMapping("/findWeFissionDetail")
    public TableDataInfo<List<WeFissionDetailVo>> findWeFissionDetail(String customerName,String weUserId){
        startPage();

        return getDataTable(
                iWeFissionService.findWeFissionDetail(customerName,weUserId)
        );
    }



    /**
     * 导出裂变明细
     * @return
     */
    @GetMapping("/exportWeFissionDetail")
    public void exportWeFissionDetail(){

        LwExcelUtil.exprotForWeb(
                ServletUtils.getResponse(), WeFissionDetailVo.class, iWeFissionService.findWeFissionDetail(null,null),"裂变明细"
        );

    }





    /**
     * 裂变明细sub
     * @param fissionDetailId
     * @return
     */
    @GetMapping("/findWeFissionDetailSub")
    public TableDataInfo<List<WeFissionDetailSubVo>> findWeFissionDetailSub(Long fissionDetailId){
        startPage();

        return getDataTable(
                iWeFissionService.findWeFissionDetailSub(fissionDetailId)
        );

    }





}
