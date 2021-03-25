package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.wecom.domain.WeMessagePush;
import com.linkwechat.wecom.service.IWeMessagePushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息发送的Controller
 *
 * @author ruoyi
 * @date 2020-10-28
 */
@RestController
@RequestMapping("/wecom/push")
public class WeMessagePushController extends BaseController {

    @Autowired
    private IWeMessagePushService weMessagePushService;

    /**
     * 查询消息发送的列表
     */
    //  @PreAuthorize("@ss.hasPermi('system:push:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeMessagePush weMessagePush) {
        startPage();
        List<WeMessagePush> list = weMessagePushService.selectWeMessagePushList(weMessagePush);
        return getDataTable(list);
    }

    /**
     * 导出消息发送的列表
     */
    // @PreAuthorize("@ss.hasPermi('system:push:export')")
    @Log(title = "消息发送的", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(WeMessagePush weMessagePush) {
        List<WeMessagePush> list = weMessagePushService.selectWeMessagePushList(weMessagePush);
        ExcelUtil<WeMessagePush> util = new ExcelUtil<WeMessagePush>(WeMessagePush.class);
        return util.exportExcel(list, "push");
    }

    /**
     * 获取消息发送的详细信息
     */
    //  @PreAuthorize("@ss.hasPermi('system:push:query')")
    @GetMapping(value = "/{messagePushId}")
    public AjaxResult getInfo(@PathVariable("messagePushId") Long messagePushId) {
        return AjaxResult.success(weMessagePushService.selectWeMessagePushById(messagePushId));
    }

    /**
     * 新增消息发送的
     */
    //  @PreAuthorize("@ss.hasPermi('system:push:add')")
    @Log(title = "消息发送的", businessType = BusinessType.INSERT)
    @PostMapping(value = "add")
    public AjaxResult add(@RequestBody WeMessagePush weMessagePush) {
        weMessagePushService.insertWeMessagePush(weMessagePush);
        return AjaxResult.success();
    }

    /**
     * 删除消息发送的
     */
    //  @PreAuthorize("@ss.hasPermi('system:push:remove')")
    @Log(title = "消息发送的", businessType = BusinessType.DELETE)
    @DeleteMapping("/{messagePushIds}")
    public AjaxResult remove(@PathVariable Long[] messagePushIds) {
        return toAjax(weMessagePushService.deleteWeMessagePushByIds(messagePushIds));
    }
}
