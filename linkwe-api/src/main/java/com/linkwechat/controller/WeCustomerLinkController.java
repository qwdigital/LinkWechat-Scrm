package com.linkwechat.controller;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.poi.LwExcelUtil;
import com.linkwechat.domain.WeCustomerLink;
import com.linkwechat.domain.WeCustomerLinkCount;
import com.linkwechat.domain.customer.vo.WeCustomerLinkCountTableVo;
import com.linkwechat.service.IWeCustomerLinkCountService;
import com.linkwechat.service.IWeCustomerLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 获客助手
 */
@Slf4j
@RestController
@RequestMapping("/customerlink")
public class WeCustomerLinkController  extends BaseController {


    @Autowired
    private IWeCustomerLinkService iWeCustomerLinkService;


    @Autowired
    private IWeCustomerLinkCountService iWeCustomerLinkCountService;


    /**
     * 获取获客助手列表
     * @param weCustomerLink
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo<WeCustomerLink> list(WeCustomerLink weCustomerLink){
        startPage();
        return getDataTable(
                iWeCustomerLinkService.list(new LambdaQueryWrapper<WeCustomerLink>()
                        .like(StringUtils.isNotEmpty(weCustomerLink.getLinkName()),WeCustomerLink::getLinkName,weCustomerLink.getLinkName())
                        .orderByDesc(WeCustomerLink::getUpdateTime))
        );

    }


    /**
     * 获取详情
     * @param id
     * @return
     */
    @GetMapping("/getCustomerLinkById/{id}")
    public AjaxResult<WeCustomerLink> getCustomerLinkById(@PathVariable Long id){


        return AjaxResult.success(
                iWeCustomerLinkService.findWeCustomerLinkById(id)
        );
    }


    /**
     * 创建获客助手
     * @param customerLink
     * @return
     */
    @PostMapping("/createCustomerLink")
    public AjaxResult createCustomerLink(@RequestBody WeCustomerLink customerLink){


        iWeCustomerLinkService.createOrUpdateCustomerLink(customerLink,true);


        return  AjaxResult.success(customerLink);
    }





    /**
     * 更新获客助手
     * @param customerLink
     * @return
     */
    @PostMapping("/updateCustomerLink")
    public AjaxResult updateCustomerLink(@RequestBody WeCustomerLink customerLink){


        iWeCustomerLinkService.createOrUpdateCustomerLink(customerLink,false);

        return  AjaxResult.success(customerLink);
    }



    /**
     * 删除获客助手链接
     */
    @DeleteMapping("/{ids}")
    public AjaxResult batchRemove(@PathVariable Long[] ids) {
        iWeCustomerLinkService.removeLink(
                ListUtil.toList(ids)
        );
        return AjaxResult.success();
    }


    /**
     *  获取链接统计折线图
     * @param linkCount
     * @return
     */
    @GetMapping("/selectLinkCountTrend")
    public AjaxResult  selectLinkCountTrend(WeCustomerLinkCount linkCount){


        return AjaxResult.success(
                iWeCustomerLinkCountService.selectLinkCountTrend(linkCount.getLinkId(), linkCount.getBeginTime(), linkCount.getEndTime())
        );
    }


    /**
     * 获取链接统计tab
     * @param linkId
     * @return
     */
    @GetMapping("/selectLinkCountTab/{linkId}")
    public AjaxResult selectLinkCountTab(@PathVariable String linkId){

        return AjaxResult.success(
                iWeCustomerLinkCountService.selectLinkCountTab(linkId)
        );
    }


    /**
     * 获取统计分页列表
     * @return
     */
    @GetMapping("/selectLinkCountTable")
    public TableDataInfo selectLinkCountTable(WeCustomerLinkCount linkCount){
        startPage();
        return getDataTable(
                iWeCustomerLinkCountService.selectLinkCountTable(linkCount.getLinkId(), linkCount.getBeginTime(), linkCount.getEndTime())
        );

    }


    /**
     *  获客助手数据报表
     * @param linkCount
     */
    @GetMapping("/exportLinkCount")
    public void exportLinkCount(WeCustomerLinkCount linkCount){

        LwExcelUtil.exprotForWeb(
                ServletUtils.getResponse(),WeCustomerLinkCountTableVo.class,iWeCustomerLinkCountService.selectLinkCountTable(linkCount.getLinkId(), linkCount.getBeginTime(), linkCount.getEndTime())
                ,"获客助手数据报表"
        );
    }


    /**
     * 同步统计数据
     * @param id
     * @return
     */
    @GetMapping("/synchCustomerCount/{id}")
    public AjaxResult synchCustomerCount(@PathVariable Long id){

        WeCustomerLink weCustomerLink = iWeCustomerLinkService.getById(id);

        if(null != weCustomerLink && StringUtils.isNotEmpty(weCustomerLink.getLinkId())){
            iWeCustomerLinkCountService.synchWeCustomerLinkCount(weCustomerLink.getLinkId());
        }

        return AjaxResult.success();

    }


    /**
     * 获取获客链接下对应的客户
     * @param weCustomerLinkCount
     * @return
     */
    @GetMapping("/findLinkWeCustomer")
    public TableDataInfo findLinkWeCustomer(WeCustomerLinkCount weCustomerLinkCount){
        startPage();

        return getDataTable(
                iWeCustomerLinkService.findLinkWeCustomer(weCustomerLinkCount)
        );
    }


    /**
     * 同步获客助手剩余量
     * @return
     */
    @GetMapping("/synchAcquisitionQuota")
    public AjaxResult synchAcquisitionQuota(){
        iWeCustomerLinkCountService.synchAcquisitionQuota();
        return AjaxResult.success();
    }



}
