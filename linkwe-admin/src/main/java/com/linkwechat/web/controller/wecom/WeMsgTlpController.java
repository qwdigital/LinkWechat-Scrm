package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.WeMsgTlp;
import com.linkwechat.wecom.domain.WeMsgTlpScope;
import com.linkwechat.wecom.service.IWeMsgTlpScopeService;
import com.linkwechat.wecom.service.IWeMsgTlpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 欢迎语模板Controller
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
@RestController
@RequestMapping("/wecom/tlp")
public class WeMsgTlpController extends BaseController
{
    @Autowired
    private IWeMsgTlpService weMsgTlpService;


    @Autowired
    private IWeMsgTlpScopeService iWeMsgTlpScopeService;

    /**
     * 查询欢迎语模板列表
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:tlp:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeMsgTlp weMsgTlp)
    {
        startPage();
        List<WeMsgTlp> list = weMsgTlpService.selectWeMsgTlpList(weMsgTlp);
        return getDataTable(list);
    }


    /**
     * 获取欢迎语模板详细信息
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:tlp:query')")
    @GetMapping(value = "/scop/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {

        return AjaxResult.success(
                iWeMsgTlpScopeService.selectWeMsgTlpScopeList(WeMsgTlpScope.builder().msgTlpId(id).build())
        );
    }

    /**
     * 新增欢迎语模板
     */
    //  @PreAuthorize("@ss.hasPermi('wecom:tlp:add')")
    @Log(title = "新增欢迎语模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WeMsgTlp weMsgTlp)
    {
        return toAjax(weMsgTlpService.insertWeMsgTlp(weMsgTlp));
    }

    /**
     * 修改欢迎语模板
     */
    //  @PreAuthorize("@ss.hasPermi('wecom:tlp:edit')")
    @Log(title = "修改欢迎语模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WeMsgTlp weMsgTlp)
    {
        return toAjax(weMsgTlpService.updateWeMsgTlp(weMsgTlp));
    }

    /**
     * 删除欢迎语模板
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:tlp:remove')")
    @Log(title = "删除欢迎语模板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(weMsgTlpService.batchRemoveByids(Arrays.asList(ids)));
    }
}
