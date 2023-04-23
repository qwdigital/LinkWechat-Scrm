package com.linkwechat.controller;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.common.utils.poi.LwExcelUtil;
import com.linkwechat.domain.operation.query.WeOperationCustomerQuery;
import com.linkwechat.domain.operation.query.WeOperationGroupQuery;
import com.linkwechat.domain.operation.vo.*;
import com.linkwechat.service.IWeOperationCenterService;
import com.linkwechat.service.IWeQrCodeService;
import com.linkwechat.service.IWeSynchRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author danmo
 * @description 运营中心
 * @date 2022/1/9 16:57
 **/
@Api(tags = "运营中心管理")
@Slf4j
@RestController
@RequestMapping("operation")
public class WeOperationCenterController extends BaseController {

    @Autowired
    private IWeOperationCenterService weOperationCenterService;

    /**
     *  客户数据分析
     * @return
     */
    @GetMapping("/customer/getAnalysis")
    public AjaxResult<WeCustomerAnalysisVo> getCustomerAnalysis() {
        return AjaxResult.success(weOperationCenterService.getCustomerAnalysis());
    }

    /**
     * 客户数据-客户总数
     * @param query
     * @return
     */
    @GetMapping("/customer/getTotalCnt")
    public AjaxResult<List<WeCustomerTotalCntVo>> getCustomerTotalCnt(WeOperationCustomerQuery query) {
        return AjaxResult.success(weOperationCenterService.getCustomerTotalCnt(query));
    }


    /**
     *  客户数据-实时数据
     * @param query
     * @return
     */
    @GetMapping("/customer/getRealCnt")
    public AjaxResult<List<WeCustomerRealCntVo>> getCustomerRealCnt(WeOperationCustomerQuery query) {
        return AjaxResult.success(weOperationCenterService.getCustomerRealCnt(query));
    }


    /**
     *  客户数据-员工客户排行
     * @param query
     * @return
     */
    @GetMapping("/customer/getRankCnt")
    public AjaxResult<List<WeUserCustomerRankVo>> getCustomerRank(WeOperationCustomerQuery query) {
        return AjaxResult.success(weOperationCenterService.getCustomerRank(query));
    }


    /**
     *  客户数据-实时数据（分页）
     * @param query
     * @return
     */
    @GetMapping("/customer/getCustomerRealPageCnt")
    public TableDataInfo<WeCustomerRealCntVo> getCustomerRealPageCnt(WeOperationCustomerQuery query) {
        startPage();
        return getDataTable(weOperationCenterService.getCustomerRealCntPage(query));
    }


    /**
     *  客户数据-实时数据-导出
     * @param query
     * @return
     */
    @GetMapping("/customer/real/export")
    public void customerRealExport(WeOperationCustomerQuery query) {
        LwExcelUtil.exprotForWeb(
                ServletUtils.getResponse(), WeCustomerRealCntVo.class, weOperationCenterService.getCustomerRealCnt(query),"客户实时数据"
        );
    }


    /**
     *  客群数据分析
     * @return
     */
    @GetMapping("/group/getAnalysis")
    public AjaxResult<WeGroupAnalysisVo> getGroupAnalysis() {
        return AjaxResult.success(weOperationCenterService.getGroupAnalysis());
    }


    /**
     * 客群数据-客群总数
     * @param query
     * @return
     */
    @GetMapping("/group/getTotalCnt")
    public AjaxResult<List<WeGroupTotalCntVo>> getGroupTotalCnt(WeOperationGroupQuery query) {
        return AjaxResult.success(weOperationCenterService.getGroupTotalCnt(query));
    }


    /**
     *  客群数据-客群成员总数
     * @param query
     * @return
     */
    @GetMapping("/group/member/getTotalCnt")
    public AjaxResult<List<WeGroupTotalCntVo>> getGroupMemberTotalCnt(WeOperationGroupQuery query) {
        return AjaxResult.success(weOperationCenterService.getGroupMemberTotalCnt(query));
    }


    /**
     * 客群数据-客群实时数据
     * @param query
     * @return
     */
    @GetMapping("/group/getRealCnt")
    public AjaxResult<List<WeGroupRealCntVo>> getGroupRealCnt(WeOperationGroupQuery query) {
        return AjaxResult.success(weOperationCenterService.getGroupRealCnt(query));
    }


    /**
     *  客群数据-客群实时数据(分页)
     * @param query
     * @return
     */
    @GetMapping("/group/getGroupRealPageCnt")
    public TableDataInfo<WeGroupRealCntVo> getGroupRealPageCnt(WeOperationGroupQuery query) {
        startPage();
        return getDataTable(weOperationCenterService.getGroupRealCnt(query));
    }


    /**
     * 客群数据-客群成员实时数据
     * @param query
     * @return
     */
    @GetMapping("/group/member/getRealCnt")
    public AjaxResult<List<WeGroupMemberRealCntVo>> getGroupMemberRealCnt(WeOperationGroupQuery query) {
        return AjaxResult.success(weOperationCenterService.getGroupMemberRealCnt(query));
    }


    /**
     * 客群数据-客群成员实时数据(分页)
     * @param query
     * @return
     */
    @GetMapping("/group/member/getGroupMemberRealPageCnt")
    public TableDataInfo<WeGroupMemberRealCntVo> getGroupMemberRealPageCnt(WeOperationGroupQuery query) {
        startPage();
//        return getDataTable(weOperationCenterService.getGroupMemberRealCnt(query));
        return getDataTable(weOperationCenterService.selectGroupMemberBrokenLine(query));
    }


    /**
     * 客群数据-客群实时数据-导出
     * @param query
     * @return
     */
    @GetMapping("/group/real/export")
    public void groupRealExport(WeOperationGroupQuery query) {
        LwExcelUtil.exprotForWeb(
                ServletUtils.getResponse(), WeGroupRealCntVo.class, weOperationCenterService.getGroupRealCnt(query),"客群实时数据"
        );
    }




    /**
     * 客群数据-客群成员实时数据-导出
     * @param query
     * @return
     */
    @GetMapping("/group/member/real/export")
    public void groupMemberRealExport(WeOperationGroupQuery query) {
        LwExcelUtil.exprotForWeb(
                ServletUtils.getResponse(), WeGroupMemberRealCntVo.class, weOperationCenterService.getGroupMemberRealCnt(query),"客群成员实时数据"
        );
    }


    /**
     * 会话分析-客户联系总数
     * @return
     */
    @GetMapping("/session/customer/getAnalysis")
    public AjaxResult<WeSessionCustomerAnalysisVo> getCustomerSessionAnalysis() {
        return AjaxResult.success(weOperationCenterService.getCustomerSessionAnalysis());
    }


    /**
     * 会话分析-客户会话总数
     * @param query
     * @return
     */
    @GetMapping("/session/customer/getTotalCnt")
    public AjaxResult<List<WeSessionCustomerTotalCntVo>> getCustomerSessionTotalCnt(WeOperationCustomerQuery query) {
        return AjaxResult.success(weOperationCenterService.getCustomerSessionTotalCnt(query));
    }


    /**
     * 会话分析-客户会话总数(分页)
     * @param query
     * @return
     */
    @GetMapping("/session/customer/getTotalPageCnt")
    public TableDataInfo<WeSessionCustomerTotalCntVo> getCustomerSessionTotalPageCnt(WeOperationCustomerQuery query) {
        startPage();
        return getDataTable(weOperationCenterService.getCustomerSessionTotalCnt(query));
    }


    /**
     * 会话分析-员工单聊总数
     * @param query
     * @return
     */
    @GetMapping("/session/customer/getUserChatRank")
    public AjaxResult<List<WeSessionUserChatRankVo>> getUserChatRank(WeOperationCustomerQuery query) {
        return AjaxResult.success(weOperationCenterService.getUserChatRank(query));
    }


    /**
     * 会话分析-员工平均恢复时长
     * @param query
     * @return
     */
    @GetMapping("/session/customer/getUserAvgReplyTimeRank")
    public AjaxResult<List<WeSessionUserAvgReplyTimeRankVo>> getUserAvgReplyTimeRank(WeOperationCustomerQuery query) {
        return AjaxResult.success(weOperationCenterService.getUserAvgReplyTimeRank(query));
    }



    /**
     * 客群数据-会话总数-导出
     * @param query
     * @return
     */
    @GetMapping("/session/customer/total/export")
    public void customerSessionTotalExport(WeOperationCustomerQuery query) {
        LwExcelUtil.exprotForWeb(
                ServletUtils.getResponse(), WeSessionCustomerTotalCntVo.class,weOperationCenterService.getCustomerSessionTotalCnt(query),"会话总数"
        );
    }


    /**
     * 会话分析-客群联系总数
     * @return
     */
    @GetMapping("/session/group/getAnalysis")
    public AjaxResult<WeSessionGroupAnalysisVo> getGroupSessionAnalysis() {
        return AjaxResult.success(weOperationCenterService.getGroupSessionAnalysis());
    }


    /**
     * 会话分析-群聊会话总数
     * @param query
     * @return
     */
    @GetMapping("/session/group/getTotalCnt")
    public AjaxResult<List<WeSessionGroupTotalCntVo>> getGroupSessionTotalCnt(WeOperationGroupQuery query) {
        return AjaxResult.success(weOperationCenterService.getGroupSessionTotalCnt(query));
    }


    /**
     * 会话分析-群聊会话总数(分页)
     * @param query
     * @return
     */
    @GetMapping("/session/group/getTotalPageCnt")
    public TableDataInfo<WeSessionGroupTotalCntVo> getGroupSessionTotalPageCnt(WeOperationGroupQuery query) {
        startPage();
        return getDataTable(weOperationCenterService.getGroupSessionTotalCnt(query));
    }

    /**
     * 会话分析-群聊会话总数-导出
     * @param query
     * @return
     */
    @GetMapping("/session/group/total/export")
    public AjaxResult getGroupSessionTotalExport(WeOperationGroupQuery query) {
        List<WeSessionGroupTotalCntVo> groupSessionTotalCnt = weOperationCenterService.getGroupSessionTotalCnt(query);
        ExcelUtil<WeSessionGroupTotalCntVo> util = new ExcelUtil<WeSessionGroupTotalCntVo>(WeSessionGroupTotalCntVo.class);
        return util.exportExcel(groupSessionTotalCnt, "会话总数");
    }

    /**
     * 会话存档数据分析
     * @return
     */
    @GetMapping("/session/archive/getAnalysis")
    public AjaxResult<WeSessionArchiveAnalysisVo> getSessionArchiveAnalysis() {
        return AjaxResult.success(weOperationCenterService.getSessionArchiveAnalysis());
    }

    /**
     * 会话存档同意明细
     * @param query
     * @return
     */
    @GetMapping("/session/archive/details")
    public TableDataInfo<WeSessionArchiveDetailVo> getSessionArchiveDetails(BaseEntity query) {
        startPage();
        return getDataTable(weOperationCenterService.getSessionArchiveDetails(query));
    }


    /**
     * 自建应用获取客户分析数据
     * @param dataScope 个人数据:false 全部数据(相对于角色定义的数据权限):true
     * @return
     */
    @GetMapping("/getCustomerAnalysisForApp")
    public AjaxResult<WeCustomerAnalysisVo> getCustomerAnalysisForApp(@RequestParam(defaultValue = "false") boolean dataScope){


        return AjaxResult.success(
                weOperationCenterService.getCustomerAnalysisForApp(dataScope)
        );

    }


    /**
     * 自建应用获取群分析数据
     * @param dataScope 个人数据:false 全部数据(相对于角色定义的数据权限):true
     * @return
     */
    @GetMapping("/getGroupAnalysisByApp")
    public AjaxResult<WeGroupAnalysisVo> getGroupAnalysisByApp(@RequestParam(defaultValue = "false") boolean dataScope){

        return AjaxResult.success(
                weOperationCenterService.getGroupAnalysisByApp(dataScope)
        );
    }



}
