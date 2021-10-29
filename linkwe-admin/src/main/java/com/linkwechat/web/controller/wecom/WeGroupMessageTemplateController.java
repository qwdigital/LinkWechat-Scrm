package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.WeGroupMessageTemplate;
import com.linkwechat.wecom.domain.query.WeAddGroupMessageQuery;
import com.linkwechat.wecom.domain.vo.WeGroupMessageDetailVo;
import com.linkwechat.wecom.domain.vo.WeGroupMessageListVo;
import com.linkwechat.wecom.service.IWeGroupMessageTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 群发消息模板Controller
 *
 * @author ruoyi
 * @date 2021-10-27
 */
@Api(tags = "群发消息管理")
@RestController
@RequestMapping("/wecom/groupmsg/template")
public class WeGroupMessageTemplateController extends BaseController {

    @Autowired
    private IWeGroupMessageTemplateService iWeGroupMessageTemplateService;

    /**
     * 查询群发消息模板列表
     */
    //@PreAuthorize("@ss.hasPermi('linkwechat:template:list')")
    @ApiOperation(value = "查询群发消息模板列表", httpMethod = "GET")
    @GetMapping("/list")
    public TableDataInfo<WeGroupMessageTemplate> list(WeGroupMessageTemplate weGroupMessageTemplate) {
        startPage();
        List<WeGroupMessageTemplate> list = iWeGroupMessageTemplateService.queryList(weGroupMessageTemplate);
        return getDataTable(list);
    }


    /**
     * 获取群发消息模板详细信息
     */
    //@PreAuthorize("@ss.hasPermi('linkwechat:template:query')")
    @ApiOperation(value = "获取群发消息模板详细信息", httpMethod = "GET")
    @GetMapping(value = "/{id}")
    public AjaxResult<WeGroupMessageDetailVo> getGroupMsgTemplateDetail(@PathVariable("id") Long id) {
        return AjaxResult.success(iWeGroupMessageTemplateService.getGroupMsgTemplateDetail(id));
    }

    /**
     * 新增群发消息模板
     */
    //@PreAuthorize("@ss.hasPermi('linkwechat:template:add')")
    @ApiOperation(value = "新增群发消息模板", httpMethod = "POST")
    @Log(title = "群发消息模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addGroupMsgTemplate(@RequestBody WeAddGroupMessageQuery query) {
        iWeGroupMessageTemplateService.addGroupMsgTemplate(query);
        return AjaxResult.success();
    }


    /**
     * 删除群发消息模板
     */
    //@PreAuthorize("@ss.hasPermi('linkwechat:template:remove')")
    @ApiOperation(value = "取消发送", httpMethod = "DELETE")
    @Log(title = "群发消息模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iWeGroupMessageTemplateService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
