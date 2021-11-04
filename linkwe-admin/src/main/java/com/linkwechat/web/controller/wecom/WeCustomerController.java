package com.linkwechat.web.controller.wecom;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.vo.WeMakeCustomerTag;
import com.linkwechat.wecom.service.IWeCustomerService;
import com.linkwechat.wecom.service.IWeCustomerTrajectoryService;
import com.linkwechat.wecom.service.IWeFlowerCustomerTagRelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 企业微信客户Controller
 *
 * @author ruoyi
 * @date 2020-09-13
 */
@RestController
@RequestMapping("/wecom/customer")
public class WeCustomerController extends BaseController
{

    @Autowired
    @Lazy
    private IWeCustomerService weCustomerService;


    @Autowired
    private IWeCustomerTrajectoryService iWeCustomerTrajectoryService;



    /**
     * 查询企业微信客户列表
     */
    //  @PreAuthorize("@ss.hasPermi('customerManage:customer:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeCustomer weCustomer)
    {
        startPage();
        List<WeCustomer> list = weCustomerService.selectWeCustomerList(weCustomer);
        return getDataTable(list);
    }


    /**
     * 查询企业微信客户列表
     */
    //  @PreAuthorize("@ss.hasPermi('customerManage:customer:list')")
    @GetMapping("/findWeCustomerList")
    @ApiOperation("查询企业微信客户列表(重构版)")
    public TableDataInfo findWeCustomerList(WeCustomerList weCustomerList)
    {
        startPage();
        List<WeCustomerList> list = weCustomerService.findWeCustomerList(weCustomerList);

        return getDataTable(list);
    }





    /**
     * 根据员工ID获取客户
     * @return
     */
    //  @PreAuthorize("@ss.hasPermi('customerManage:customer:list')")
    @GetMapping("/getCustomersByUserId/{externalUserid}")
    public AjaxResult getCustomersByUserId(@PathVariable String externalUserid){


         return AjaxResult.success(weCustomerService.getCustomersByUserId(externalUserid));
    }





//    /**
//     * 导出企业微信客户列表
//     */
//    //   @PreAuthorize("@ss.hasPermi('wecom:customer:export')")
//    @Log(title = "企业微信客户", businessType = BusinessType.EXPORT)
//    @GetMapping("/export")
//    public AjaxResult export(WeCustomer weCustomer)
//    {
//        List<WeCustomer> list = weCustomerService.selectWeCustomerList(weCustomer);
//        ExcelUtil<WeCustomer> util = new ExcelUtil<WeCustomer>(WeCustomer.class);
//        return util.exportExcel(list, "customer");
//    }

//    /**
//     * 获取企业微信客户详细信息
//     */
//    //  @PreAuthorize("@ss.hasPermi('customerManage:customer:view')")
//    @GetMapping(value = "/{externalUserId}")
//    public AjaxResult getInfo(@PathVariable("externalUserId") String externalUserId)
//    {
//        return AjaxResult.success(weCustomerService.selectWeCustomerById(externalUserId));
//    }



    /**
     * 修改企业微信客户(目前只更新生日)
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:customer:edit')")
    @Log(title = "企业微信客户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody WeCustomer weCustomer)
    {
        weCustomerService.update(weCustomer,new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getExternalUserid,weCustomer.getExternalUserid())
                .eq(WeCustomer::getFirstUserId,weCustomer.getFirstUserId()));

        return AjaxResult.success();
    }


    /**
     * 客户同步接口
     * @return
     */
    //   @PreAuthorize("@ss.hasPermi('customerManage:customer:sync')")
    @Log(title = "企业微信客户同步接口", businessType = BusinessType.DELETE)
    @GetMapping("/synchWeCustomer")
    public AjaxResult synchWeCustomer() {
        try {
            weCustomerService.synchWeCustomer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.success(WeConstans.SYNCH_TIP);

    }


    /**
     * 客户打标签
     * @param weMakeCustomerTag
     * @return
     */
    //   @PreAuthorize("@ss.hasPermi('customerManage/customer:makeTag')")
    @Log(title = "客户打标签", businessType = BusinessType.UPDATE)
    @PostMapping("/makeLabel")
    public AjaxResult makeLabel(@RequestBody WeMakeCustomerTag weMakeCustomerTag){

        weCustomerService.makeLabel(weMakeCustomerTag);

        return AjaxResult.success();
    }


    /**
     * 移除客户标签
     * @return
     */
    //   @PreAuthorize("@ss.hasPermi('customerManage:customer:removeTag')")
    @Log(title = "移除客户标签", businessType = BusinessType.DELETE)
    @DeleteMapping("/removeLabel")
    public AjaxResult removeLabel(@RequestBody WeMakeCustomerTag weMakeCustomerTag){

        weCustomerService.makeLabel(weMakeCustomerTag);

        return AjaxResult.success();

    }


    /*************************************************************************
     ******************************客户详情相关*********************************
     *************************************************************************/

    /**
     * 客户详情基础数据
     * @return
     */
    @ApiOperation("客户详情基础数据")
    @GetMapping(value = "/findCustomerDetailBaseInfo")
    public AjaxResult findCustomerDetailBaseInfo(String externalUserid){

        return AjaxResult.success(
                weCustomerService.findWeCustomerDetail(externalUserid)
        );

    }


    /**
     * 获取客户轨迹信息
     * @param trajectoryType
     * @return
     */
    @GetMapping(value = "/findTrajectory")
    @ApiOperation("获取客户轨迹信息")
    public TableDataInfo findTrajectory(String userId,Integer trajectoryType){
        startPage();
        LambdaQueryWrapper<WeCustomerTrajectory> ne = new LambdaQueryWrapper<WeCustomerTrajectory>()
                .ne(WeCustomerTrajectory::getStatus, Constants.DELETE_CODE)
                .eq(WeCustomerTrajectory::getUserId,userId);
        if(trajectoryType != null){
            ne.eq(WeCustomerTrajectory::getTrajectoryType, trajectoryType);
        }
        return getDataTable(
                iWeCustomerTrajectoryService.list(ne)
        );
    }

    /**
     * 客户条件检索
     * @param params 检索条件
     * @return
     */
    @ApiOperation(value = "客户条件检索",httpMethod = "POST")
    @PostMapping(value = "/getCustomerByCondition")
    public AjaxResult getCustomerByCondition(@RequestBody JSONObject params){
        weCustomerService.getCustomerByCondition(params);
        return AjaxResult.success("");
    }

}
