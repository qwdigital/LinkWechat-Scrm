package com.linkwechat.web.controller.wecom;

import com.github.pagehelper.PageInfo;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.wecom.domain.WeGroupMessageList;
import com.linkwechat.wecom.domain.vo.WeGroupMessageDetailVo;
import com.linkwechat.wecom.domain.vo.WeGroupMessageListVo;
import com.linkwechat.wecom.service.IWeGroupMessageListService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 群发消息列Controller
 *
 * @author ruoyi
 * @date 2021-10-19
 */
@Api(tags = "群发消息管理")
@RestController
@RequestMapping("/wecom/group/message")
public class WeGroupMessageListController extends BaseController {

    @Autowired
    private IWeGroupMessageListService iWeGroupMessageListService;

    /**
     * 查询群发消息列列表
     */
    //@PreAuthorize("@ss.hasPermi('linkwechat:group:message:list')")
    @GetMapping("/list")
    public TableDataInfo<WeGroupMessageListVo> list(WeGroupMessageList weGroupMessageList) {
        startPage();
        PageInfo<WeGroupMessageListVo> listPageInfo = iWeGroupMessageListService.queryList(weGroupMessageList);
        return getDataTable(listPageInfo);
    }

    /**
     * 导出群发消息列列表
     */
    @PreAuthorize("@ss.hasPermi('linkwechat:group:message:list:export')")
    @Log(title = "群发消息列", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(WeGroupMessageList weGroupMessageList) {
        PageInfo<WeGroupMessageListVo> listPageInfo = iWeGroupMessageListService.queryList(weGroupMessageList);
        ExcelUtil<WeGroupMessageListVo> util = new ExcelUtil<>(WeGroupMessageListVo.class);
        return util.exportExcel(listPageInfo.getList(), "list");
    }

    /**
     * 获取群发消息列详细信息
     */
    //@PreAuthorize("@ss.hasPermi('linkwechat:group:message:list:query')")
    @GetMapping(value = "/{msgId}")
    public AjaxResult<WeGroupMessageDetailVo> getInfo(@PathVariable("msgId") String msgId) {
        return AjaxResult.success(iWeGroupMessageListService.getGroupMsgDetail(msgId));
    }

    /**
     * 新增群发消息列
     */
    @PreAuthorize("@ss.hasPermi('linkwechat:group:message:list:add')")
    @Log(title = "群发消息列", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WeGroupMessageList weGroupMessageList) {
        return toAjax(iWeGroupMessageListService.save(weGroupMessageList) ? 1 : 0);
    }

    /**
     * 修改群发消息列
     */
    @PreAuthorize("@ss.hasPermi('linkwechat:group:message:list:edit')")
    @Log(title = "群发消息列", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WeGroupMessageList weGroupMessageList) {
        return toAjax(iWeGroupMessageListService.updateById(weGroupMessageList) ? 1 : 0);
    }

    /**
     * 删除群发消息列
     */
    @PreAuthorize("@ss.hasPermi('linkwechat:group:message:list:remove')")
    @Log(title = "群发消息列", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iWeGroupMessageListService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
