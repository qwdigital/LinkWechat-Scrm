package com.linkwechat.controller;

import com.alibaba.excel.EasyExcel;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.poi.LwExcelUtil;
import com.linkwechat.domain.WeChatContactMsg;
import com.linkwechat.domain.msgaudit.query.WeChatContactMsgQuery;
import com.linkwechat.domain.msgaudit.vo.WeChatContactMsgVo;
import com.linkwechat.service.IWeChatContactMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 会话消息Controller
 * 
 * @author ruoyi
 * @date 2021-07-28
 */
@Api(tags = "会话存档管理")
@RestController
@RequestMapping("/chat/msg" )
public class WeChatContactMsgController extends BaseController {

    @Autowired
    private IWeChatContactMsgService iWeChatContactMsgService;

    /**
     * 查询会话消息列表
     */
    @ApiOperation("查询会话消息列表")
    @GetMapping("/list")
    public TableDataInfo<List<WeChatContactMsg>> list(WeChatContactMsg weChatContactMsg) {
        startPage();
        List<WeChatContactMsg> list = iWeChatContactMsgService.queryList(weChatContactMsg);
        return getDataTable(list);
    }

    /**
     * 导出会话消息列表
     */
    @ApiOperation("导出会话消息列表")
    @Log(title = "会话消息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public void export(WeChatContactMsg weChatContactMsg) throws IOException {
        List<WeChatContactMsg> list = iWeChatContactMsgService.queryList(weChatContactMsg);
        HttpServletResponse response = ServletUtils.getResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("会话消息", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), WeChatContactMsg.class).sheet("会话消息").doWrite(list);
    }

    /**
     * 获取会话消息详细信息
     */
    @ApiOperation("获取会话消息详细信息")
    @GetMapping(value = "/{id}" )
    public AjaxResult<WeChatContactMsg> getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iWeChatContactMsgService.getById(id));
    }


    /**
     * 外部联系人/单聊 会话列表
     */
    @ApiOperation("外部联系人 会话列表")
    @Log(title = "外部联系人 会话列表" , businessType = BusinessType.OTHER)
    @GetMapping("/selectExternalChatList/{fromId}" )
    public AjaxResult<List<WeChatContactMsgVo>> selectExternalChatList(@PathVariable("fromId") String fromId) {
        return AjaxResult.success(iWeChatContactMsgService.selectExternalChatList(fromId));
    }

    /**
     * 外部联系人/单聊 会话列表
     */
    @ApiOperation("单聊 会话列表")
    @Log(title = "单聊 会话列表" , businessType = BusinessType.OTHER)
    @GetMapping("/selectAloneChatList" )
    public AjaxResult<List<WeChatContactMsgVo>> selectAloneChatList(WeChatContactMsg weChatContactMsg) {
        return AjaxResult.success(iWeChatContactMsgService.selectAloneChatList(weChatContactMsg));
    }

    /**
     * 内部联系人 会话列表
     */
    @ApiOperation("内部联系人 会话列表")
    @Log(title = "内部联系人 会话列表" , businessType = BusinessType.OTHER)
    @GetMapping("/selectInternalChatList/{fromId}" )
    public AjaxResult<List<WeChatContactMsgVo>> selectInternalChatList(@PathVariable("fromId") String fromId) {
        return AjaxResult.success(iWeChatContactMsgService.selectInternalChatList(fromId));
    }

    /**
     * 群聊 会话列表
     */
    @ApiOperation("群聊 会话列表")
    @Log(title = "群聊 会话列表" , businessType = BusinessType.OTHER)
    @GetMapping("/selectGroupChatList/{fromId}" )
    public AjaxResult<List<WeChatContactMsgVo>> selectGroupChatList(@PathVariable("fromId") String fromId) {
        return AjaxResult.success(iWeChatContactMsgService.selectGroupChatList(fromId));
    }

    /**
     * 全文检索 会话列表
     */
    @ApiOperation("全文检索 会话列表")
    @Log(title = "全文检索 会话列表" , businessType = BusinessType.OTHER)
    @GetMapping("/selectFullSearchChatList" )
    public TableDataInfo<List<WeChatContactMsgVo>> selectFullSearchChatList(WeChatContactMsgQuery query) {
        startPage();
        List<WeChatContactMsgVo> list = iWeChatContactMsgService.selectFullSearchChatList(query);
        return getDataTable(list);
    }


    /**
     * 全文检索 会话列表
     */
    @ApiOperation("全文检索 导出列表")
    @GetMapping("/selectFullSearchChatList/export")
    public void fullSearchChatListExport(WeChatContactMsgQuery query) {
        LwExcelUtil.exprotForWeb(
                ServletUtils.getResponse(), WeChatContactMsgVo.class, iWeChatContactMsgService.selectFullSearchChatList(query),"全文检索"
        );
    }
}
