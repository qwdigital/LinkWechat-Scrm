package com.linkwechat.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.domain.msgaudit.vo.WeChatContactMsgVo;
import com.linkwechat.domain.qirule.query.*;
import com.linkwechat.domain.qirule.vo.*;
import com.linkwechat.handler.WeQiRuleWeeklyUserDetailWriteHandler;
import com.linkwechat.service.IWeQiRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 会话质检管理
 *
 * @author danmo
 * @date 2023/05/05 18:22
 **/
@Slf4j
@RestController
@RequestMapping(value = "qi")
@Api(tags = "会话质检管理")
public class WeQiRuleController extends BaseController {

    @Autowired
    private IWeQiRuleService weQiRuleService;

    @ApiOperation(value = "新增质检规则", httpMethod = "POST")
    @PostMapping("/add")
    public AjaxResult addQiRule(@RequestBody @Validated WeQiRuleAddQuery query) {
        weQiRuleService.addQiRule(query);
        return AjaxResult.success();
    }

    @ApiOperation(value = "修改质检规则", httpMethod = "PUT")
    @PutMapping("/update/{id}")
    public AjaxResult updateQiRule(@PathVariable("id") Long id, @RequestBody @Validated WeQiRuleAddQuery query) {
        query.setQiId(id);
        weQiRuleService.updateQiRule(query);
        return AjaxResult.success();
    }


    @ApiOperation(value = "获取质检规则详情", httpMethod = "GET")
    @GetMapping("/get/{id}")
    public AjaxResult<WeQiRuleDetailVo> getQiRuleDetail(@PathVariable("id") Long id) {
        WeQiRuleDetailVo detail = weQiRuleService.getQiRuleDetail(id);
        return AjaxResult.success(detail);
    }

    @ApiOperation(value = "获取质检规则列表", httpMethod = "GET")
    @GetMapping("/list")
    public TableDataInfo<WeQiRuleListVo> getQiRuleList(WeQiRuleListQuery query) {
        startPage();
        PageInfo<WeQiRuleListVo> qrCodeList = weQiRuleService.getQiRulePageList(query);
        return getDataTable(qrCodeList);
    }

    @ApiOperation(value = "删除质检规则", httpMethod = "DELETE")
    @DeleteMapping("/del/{ids}")
    public AjaxResult delQiRule(@PathVariable("ids") List<Long> ids) {
        weQiRuleService.delQiRule(ids);
        return AjaxResult.success();
    }

    @ApiOperation(value = "质检数据看板统计", httpMethod = "GET")
    @GetMapping("/statistics/view/{id}")
    public AjaxResult<WeQiRuleStatisticsViewVo> qiRuleViewStatistics(@PathVariable("id") Long id) {
        WeQiRuleStatisticsViewVo viewVo = weQiRuleService.qiRuleViewStatistics(id);
        return AjaxResult.success(viewVo);
    }

    @ApiOperation(value = "质检数据列表统计", httpMethod = "GET")
    @GetMapping("/statistics/table/{id}")
    public TableDataInfo<List<WeQiRuleStatisticsTableVo>> qiRuleTableStatistics(@PathVariable("id") Long id, WeQiRuleStatisticsTableListQuery query) {
        startPage();
        query.setRuleId(id);
        List<WeQiRuleStatisticsTableVo> list = weQiRuleService.qiRuleTableStatistics(query);
        return getDataTable(list);
    }

    @ApiOperation(value = "质检数据统计聊天记录", httpMethod = "POST")
    @PostMapping("/statistics/table/msg")
    public AjaxResult<List<WeChatContactMsgVo>> getQiRuleTableStatisticsMsg(@RequestBody WeQiRuleStatisticsTableMsgQuery query) {
        List<WeChatContactMsgVo> list = weQiRuleService.getQiRuleTableStatisticsMsg(query);
        return AjaxResult.success(list);
    }

    @ApiOperation(value = "质检通知列表", httpMethod = "GET")
    @GetMapping("/notice/list")
    public TableDataInfo<List<WeQiRuleNoticeListVo>> getNoticeList(WeQiRuleNoticeListQuery query) {
        startPage();
        List<WeQiRuleNoticeListVo> list = weQiRuleService.getNoticeList(query);
        return getDataTable(list);
    }


    @ApiOperation(value = "质检通知设置回复状态", httpMethod = "PUT")
    @PutMapping("/notice/update/replyStatus/{qiRuleMsgId}")
    public AjaxResult updateReplyStatus(@PathVariable("qiRuleMsgId") Long qiRuleMsgId) {
        weQiRuleService.updateReplyStatus(qiRuleMsgId);
        return AjaxResult.success();
    }

    @ApiOperation(value = "质检周报列表", httpMethod = "GET")
    @GetMapping("/weekly/list")
    public TableDataInfo<List<WeQiRuleWeeklyListVo>> getWeeklyList(WeQiRuleWeeklyListQuery query) {
        super.startPage();
        List<WeQiRuleWeeklyListVo> list = weQiRuleService.getWeeklyList(query);
        return getDataTable(list);
    }

    @ApiOperation(value = "质检周报详情", httpMethod = "GET")
    @GetMapping("/weekly/getDetail/{id}")
    public AjaxResult<WeQiRuleWeeklyDetailVo> getWeeklyDetail(@PathVariable("id") Long id) {
        WeQiRuleWeeklyDetailVo detail = weQiRuleService.getWeeklyDetail(id);
        return AjaxResult.success(detail);
    }

    @ApiOperation(value = "质检周报明细列表", httpMethod = "GET")
    @GetMapping("/weekly/detail/list/{id}")
    public TableDataInfo<List<WeQiRuleWeeklyDetailListVo>> getWeeklyDetailList(@PathVariable("id") Long id, WeQiRuleWeeklyDetailListQuery query) {
        query.setWeeklyId(id);
        super.startPage();
        List<WeQiRuleWeeklyDetailListVo> list = weQiRuleService.getWeeklyDetailList(query);
        return getDataTable(list);
    }

    @ApiOperation(value = "质检周报明细列表导出", httpMethod = "GET")
    @GetMapping("/weekly/detail/list/export/{id}")
    public void weeklyDetailListExport(@PathVariable("id") Long id, WeQiRuleWeeklyDetailListQuery query) {
        query.setWeeklyId(id);
        List<WeQiRuleWeeklyDetailListVo> list = weQiRuleService.getWeeklyDetailList(query);
        try {
            HttpServletResponse response = ServletUtils.getResponse();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("质检周报明细", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            ExcelWriterBuilder write = EasyExcel.write(response.getOutputStream(), WeQiRuleWeeklyDetailListVo.class);
            write.relativeHeadRowIndex(1);
            write.registerWriteHandler(new WeQiRuleWeeklyUserDetailWriteHandler(query));
            write.sheet("质检周报明细").doWrite(list);
        } catch (IOException e) {
            log.error("质检周报明细列表导出异常：query:{}", JSONObject.toJSONString(query), e);
        }
    }
}
