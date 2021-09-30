package com.linkwechat.web.controller.wecom;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.wecom.domain.WeChatContactMsg;
import com.linkwechat.wecom.domain.vo.WeChatContactMsgVo;
import com.linkwechat.wecom.service.IWeChatContactMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 会话消息Controller
 * 
 * @author ruoyi
 * @date 2021-07-28
 */
@Api(tags = "会话存档管理")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/chat/msg" )
public class WeChatContactMsgController extends BaseController {

    private final IWeChatContactMsgService iWeChatContactMsgService;

    /**
     * 查询会话消息列表
     */
    @ApiOperation("查询会话消息列表")
    @PreAuthorize("@ss.hasPermi('linkwechat:msg:list')")
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
    @PreAuthorize("@ss.hasPermi('linkwechat:msg:export')" )
    @Log(title = "会话消息" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(WeChatContactMsg weChatContactMsg) {
        List<WeChatContactMsg> list = iWeChatContactMsgService.queryList(weChatContactMsg);
        ExcelUtil<WeChatContactMsg> util = new ExcelUtil<WeChatContactMsg>(WeChatContactMsg.class);
        return util.exportExcel(list, "msg" );
    }

    /**
     * 获取会话消息详细信息
     */
    @ApiOperation("获取会话消息详细信息")
    @PreAuthorize("@ss.hasPermi('linkwechat:msg:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult<WeChatContactMsg> getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iWeChatContactMsgService.getById(id));
    }

    /**
     * 新增会话消息
     */
    @ApiOperation("新增会话消息")
    @PreAuthorize("@ss.hasPermi('linkwechat:msg:add')" )
    @Log(title = "会话消息" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WeChatContactMsg weChatContactMsg) {
        return toAjax(iWeChatContactMsgService.save(weChatContactMsg) ? 1 : 0);
    }

    /**
     * 修改会话消息
     */
    @ApiOperation("修改会话消息")
    @PreAuthorize("@ss.hasPermi('linkwechat:msg:edit')" )
    @Log(title = "会话消息" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WeChatContactMsg weChatContactMsg) {
        return toAjax(iWeChatContactMsgService.updateById(weChatContactMsg) ? 1 : 0);
    }

    /**
     * 删除会话消息
     */
    @ApiOperation("删除会话消息")
    @PreAuthorize("@ss.hasPermi('linkwechat:msg:remove')" )
    @Log(title = "会话消息" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iWeChatContactMsgService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
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
    public TableDataInfo<List<WeChatContactMsgVo>> selectFullSearchChatList(WeChatContactMsg weChatContactMsg) {
        startPage();
        List<WeChatContactMsgVo> list = iWeChatContactMsgService.selectFullSearchChatList(weChatContactMsg);
        return getDataTable(list);
    }

    /**
     * 全文检索 会话列表
     */
    @ApiOperation("全文检索 导出列表")
    @Log(title = "全文检索 导出列表" , businessType = BusinessType.OTHER)
    @GetMapping("/selectFullSearchChatList/export" )
    public AjaxResult fullSearchChatListExport(WeChatContactMsg weChatContactMsg) {
        List<WeChatContactMsgVo> list = iWeChatContactMsgService.selectFullSearchChatList(weChatContactMsg);
        ExcelUtil<WeChatContactMsgVo> util = new ExcelUtil<WeChatContactMsgVo>(WeChatContactMsgVo.class);
        return util.exportExcel(list, "msg" );
    }
}
