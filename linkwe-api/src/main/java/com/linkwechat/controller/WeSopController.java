package com.linkwechat.controller;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.sop.WeSopBase;
import com.linkwechat.service.IWeSopBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/sop")
public class WeSopController extends BaseController {

    @Autowired
    private IWeSopBaseService iWeSopBaseService;


    /**
     * 获取sop列表
     * @param weSopBase  baseType sop基础类型(1:客户sop;2:客群sop)
     * @return
     */
    @GetMapping("/findWeSopLists")
    public TableDataInfo findWeSopLists(@Validated WeSopBase weSopBase){

        return iWeSopBaseService.findWeSopListsVo(weSopBase);

    }


    /**
     * 获取统计详情tab
     * @param sopBaseId
     * @return
     */
    @GetMapping("/findWeSopDetailTab/{sopBaseId}")
    public AjaxResult findWeSopDetailTabVo(@PathVariable String sopBaseId){

        return AjaxResult.success(
                iWeSopBaseService.findWeSopDetailTabVo(sopBaseId)
        );
    }


    /**
     * 获取客户内容执行列表
     * @param weUserId 执行员工id
     * @param externalUserid 客户id
     * @param executeSubState 执行子状态 1:当前sop下一条任务信息都未推送;2:当前sop下信息推送完
     * @param sopBaseId sop主键
     * @param executeTargetId
     * @return
     */
    @GetMapping("/findCustomerExecuteContent")
    public AjaxResult findCustomerExecuteContent(String weUserId, String externalUserid, Integer executeSubState, String sopBaseId,String executeTargetId){


        return AjaxResult.success(
                iWeSopBaseService.findCustomerExecuteContent(weUserId,externalUserid,executeSubState,sopBaseId,executeTargetId)
        );
    }


    /**
     * (移动端)侧边栏需要推送的个人sop
     * @param targetId
     * @param executeSubState
     * @return
     */
    @GetMapping("/findCustomerSopContent")
    public AjaxResult findCustomerSopContent(String targetId, Integer executeSubState){


        return AjaxResult.success(
                iWeSopBaseService.findCustomerSopContent(SecurityUtils.getLoginUser().getSysUser().getWeUserId(),targetId,executeSubState)
        );

    }


    /**
     *  获取客群内容执行列表
     * @param chatId 群id
     * @param executeState 执行状态
     * @param sopBaseId sop主键
     * @return
     */
    @GetMapping("/findGroupExecuteContent")
    public AjaxResult findGroupExecuteContent(String chatId, Integer executeState, String sopBaseId,String executeTargetId){

        return AjaxResult.success(
                iWeSopBaseService.findGroupExecuteContent(chatId,executeState,sopBaseId,executeTargetId)
        );
    }


    /**
     *  (移动端)侧边栏需要推送的客群sop
     * @param chatId
     * @param executeSubState
     * @return
     */
    @GetMapping("/findGroupSopContent")
    public AjaxResult findGroupSopContent(String chatId, Integer executeSubState){

        return AjaxResult.success(
                iWeSopBaseService.findGroupSopContent(chatId,executeSubState)
        );
    }


    /**
     *  获取今日客户相关sop信息
     *  isExpiringSoon true 即将过期的sop false 当日sop
     * @return
     */
    @GetMapping("/findWeCustomerSop")
    public AjaxResult findTodayWeCustomerSop(@RequestParam(defaultValue = "false") Boolean isExpiringSoon){


        return AjaxResult.success(
                iWeSopBaseService.findWeCustomerSopToBeSent(isExpiringSoon)
        );
    }

    /**
     * 获取今日客群相关sop
     *  isExpiringSoon true 即将过期的sop false 当日sop
     * @return
     */
    @GetMapping("/findTodayGroupSop")
    public AjaxResult findTodayGroupSop(@RequestParam(defaultValue = "false") Boolean isExpiringSoon){

        return AjaxResult.success(
                iWeSopBaseService.findWeGroupSopToBeSent(isExpiringSoon)
        );
    }




    /**
     *  sop统计详情客群列表
     * @param sopBaseId
     * @param groupName
     * @param executeState
     * @param weUserId
     * @return
     */
    @RequestMapping("/findWeSopDetailGroup")
    public TableDataInfo findWeSopDetailGroup(String sopBaseId,String groupName,Integer executeState, String weUserId){
        startPage();
        return getDataTable(
                iWeSopBaseService.findWeSopDetailGroup(sopBaseId, groupName, executeState, weUserId)
        );
    }




    /**
     * sop统计详情客户列表
     * @param sopBaseId
     * @param customerName
     * @param executeState
     * @param weUserId
     * @return
     */
    @GetMapping("/findWeSopDetailCustomer")
    public TableDataInfo findWeSopDetailCustomer(String sopBaseId, String customerName, Integer executeState, String weUserId){
        startPage();
        return getDataTable(
                iWeSopBaseService.findWeSopDetailCustomer(sopBaseId,customerName,executeState,weUserId)
        );
    }


    /**
     * 创建sop
     * @param weSopBase
     */
    @PostMapping("/createWeSop")
    public AjaxResult createWeSop(@RequestBody WeSopBase weSopBase){
        iWeSopBaseService.createWeSop(weSopBase);
        return AjaxResult.success(
        );
    }



    /**
     * 更新sop
     * @param weSopBase
     */
    @PutMapping("/updateWeSop")
    public AjaxResult updateWeSop(@RequestBody WeSopBase weSopBase){
        iWeSopBaseService.updateWeSop(weSopBase);
        return AjaxResult.success();
    }


    /**
     * 获取sop详情
     * @param sopBaseId
     * @return
     */
    @GetMapping("/getDetail/{sopBaseId}")
    public AjaxResult<WeSopBase> findWeSopBaseBySopBaseId(@PathVariable Long sopBaseId){

        return AjaxResult.success(
                iWeSopBaseService.findWeSopBaseBySopBaseId(sopBaseId)
        );
    }

    /**
     * 通过id列表批量删除sop
     *
     * @param ids id列表
     * @return 结果
     */
    @DeleteMapping(path = "/{ids}")
    public AjaxResult batchDeleteSop(@PathVariable("ids") Long[] ids) {
        iWeSopBaseService.removeWeSoPBySopId(Arrays.asList(ids));
        return AjaxResult.success();
    }


    /**
     * 启用停用sop
     * @return
     */
    @PutMapping("/updateSopState")
    public AjaxResult startOrStopSop(@RequestBody WeSopBase weSopBase){

        iWeSopBaseService.updateSopState(String.valueOf(weSopBase.getId()),weSopBase.getSopState());

        return AjaxResult.success();
    }


    /**
     * 执行sop
     * @param executeTargetAttachId
     * @return
     */
    @PostMapping("/executeSop/{executeTargetAttachId}")
    public AjaxResult executeSop(@PathVariable String executeTargetAttachId){
        iWeSopBaseService.executeSop(executeTargetAttachId);

        return AjaxResult.success();
    }


    /**
     * 企业发送方式结果同步
     * @param sopBaseId
     * @return
     */
    @GetMapping("/synchSopExecuteResult/{sopBaseId}")
    public AjaxResult synchSopExecuteResult(@PathVariable("sopBaseId") String sopBaseId){
        iWeSopBaseService.synchSopExecuteResultForWeChatPushType(sopBaseId);

        return AjaxResult.success();
    }




}

