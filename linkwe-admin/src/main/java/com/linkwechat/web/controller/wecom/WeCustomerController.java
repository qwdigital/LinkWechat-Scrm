package com.linkwechat.web.controller.wecom;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.core.page.TableSupport;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.constants.SynchRecordConstants;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.vo.WeMakeCustomerTag;
import com.linkwechat.wecom.domain.vo.WeOnTheJobCustomerVo;
import com.linkwechat.wecom.service.IWeCustomerService;
import com.linkwechat.wecom.service.IWeCustomerTrajectoryService;
import com.linkwechat.wecom.service.IWeSynchRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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


    @Autowired
    private IWeSynchRecordService iWeSynchRecordService;


    /**********************************************************************************
     **************************************客户列表*************************************
     *********************************************************************************/

    /**
     * 查询企业微信客户列表(分页)，优化百万数据分页查询，没有用框架自带分页
     */
    @GetMapping("/findWeCustomerList")
    @ApiOperation("查询企业微信客户列表")
    public TableDataInfo findWeCustomerList(WeCustomerList weCustomerList)
    {

        List<WeCustomerList> list = weCustomerService.findWeCustomerList(weCustomerList,TableSupport.buildPageRequest());
        TableDataInfo dataTable = getDataTable(list);
        //设置总条数
        dataTable.setTotal(
                weCustomerService.countWeCustomerList(weCustomerList)
        );
        dataTable.setLastSyncTime(
                iWeSynchRecordService.findUpdateLatestTime(SynchRecordConstants.SYNCH_CUSTOMER)
        );//最近同步时间
        dataTable.setNoRepeatCustomerTotal(
                weCustomerService.noRepeatCountCustomer(weCustomerList)
        );//去重客户数

        return dataTable;
    }


    /**
     * 查询企业微信客户列表(不分页)
     */
    @GetMapping("/findAllWeCustomerList")
    @ApiOperation("查询企业微信客户列表")
    public AjaxResult findAllWeCustomerList(WeCustomerList weCustomerList)
    {
        return AjaxResult.success(
                weCustomerService.findWeCustomerList(weCustomerList,null)
        );
    }


    /**
     * 客户同步接口
     * @return
     */
    @Log(title = "企业微信客户同步接口", businessType = BusinessType.DELETE)
    @GetMapping("/synchWeCustomer")
    public AjaxResult synchWeCustomer() {

        try {
            weCustomerService.synchWeCustomer();
        }catch (CustomException e){
           return AjaxResult.error(e.getMessage());
        }


        return AjaxResult.success(WeConstans.SYNCH_TIP);

    }



    /**
     * 客户打标签
     * @param weMakeCustomerTag
     * @return
     */
    @Log(title = "客户打标签", businessType = BusinessType.UPDATE)
    @PostMapping("/makeLabel")
    public AjaxResult makeLabel(@RequestBody WeMakeCustomerTag weMakeCustomerTag){

        weCustomerService.makeLabel(weMakeCustomerTag);

        return AjaxResult.success();
    }


    /**
     * 在职继承
     * @param weOnTheJobCustomerVo
     * @return
     */
    @Log(title="在职继承",businessType = BusinessType.UPDATE)
    @PostMapping("/jobExtends")
    public AjaxResult jobExtends(@RequestBody WeOnTheJobCustomerVo weOnTheJobCustomerVo){

        try {
            weCustomerService.allocateOnTheJobCustomer(
                    weOnTheJobCustomerVo
            );
        }catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }


        return AjaxResult.success();
    }


    /**********************************************************************************
     **************************************客户详情*************************************
     *********************************************************************************/

    /**
     *  客户详情基础(基础信息+社交关系)
     * @param externalUserid
     * @param userId
     * @param
     * @return
     */
    @GetMapping("/findWeCustomerBaseInfo")
    public AjaxResult findWeCustomerBaseInfo(String externalUserid,String userId,@RequestParam(defaultValue = "0") Integer delFlag){

        return AjaxResult.success(
                weCustomerService.findWeCustomerDetail(externalUserid,userId,delFlag)
        );
    }


    /**
     * 客户画像汇总
     * @param externalUserid
     * @return
     */
    @GetMapping("/findWeCustomerInfoSummary")
    public AjaxResult findWeCustomerInfoSummary(String externalUserid,@RequestParam(defaultValue = "0") Integer delFlag){


        return AjaxResult.success(
                weCustomerService.findWeCustomerInfoSummary(externalUserid,null,delFlag)
        );
    }


    /**
     * 单个跟进人客户
     * @param externalUserid
     * @param userId
     * @return
     */
    @GetMapping("/findWeCustomerInfoByUserId")
    public AjaxResult findWeCustomerInfoByUserId(String externalUserid,String userId,@RequestParam(defaultValue = "0") Integer delFlag){



        return AjaxResult.success(
                weCustomerService.findWeCustomerInfoByUserId(externalUserid,userId,delFlag)
        );
    }


    /**
     * 跟进记录
     * @param externalUserid
     * @param userId
     * @return
     */
    @GetMapping("/followUpRecord")
    public TableDataInfo followUpRecord(String externalUserid,String userId,Integer trajectoryType){

         startPage();

         List<WeCustomerTrajectory> weCustomerTrajectories = iWeCustomerTrajectoryService
                 .followUpRecord(externalUserid, userId, trajectoryType);

         return getDataTable(weCustomerTrajectories);
    }


    /**
     * 客户轨迹
     * @param externalUserid
     * @param userId
     * @param trajectoryType 轨迹类型:1:信息动态;2:社交动态;3:活动规则;4:待办动态
     * @return
     */
    @GetMapping("/findTrajectory")
    public AjaxResult findTrajectory(String externalUserid,String userId,Integer trajectoryType){

        List<WeCustomerTrajectory> weCustomerTrajectories = iWeCustomerTrajectoryService
                .list(new LambdaQueryWrapper<WeCustomerTrajectory>()
                        .eq(StringUtils.isNotEmpty(externalUserid),WeCustomerTrajectory::getExternalUserid, externalUserid)
                        .eq(StringUtils.isNotEmpty(userId),WeCustomerTrajectory::getUserId, userId)
                        .eq(trajectoryType !=null,WeCustomerTrajectory::getTrajectoryType,trajectoryType)
                );

         return AjaxResult.success(weCustomerTrajectories);
    }



}
