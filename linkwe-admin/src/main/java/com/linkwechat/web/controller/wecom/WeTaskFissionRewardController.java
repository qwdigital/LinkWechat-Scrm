package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.wecom.domain.WeTaskFissionReward;
import com.linkwechat.wecom.service.IWeTaskFissionRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 任务裂变奖励Controller
 *
 * @author leejoker
 * @date 2021-01-20
 */
@RestController
@RequestMapping("/wecom/reward")
public class WeTaskFissionRewardController extends BaseController {
    @Autowired
    private IWeTaskFissionRewardService weTaskFissionRewardService;

    /**
     * 查询任务裂变奖励列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:reward:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeTaskFissionReward weTaskFissionReward) {
        startPage();
        List<WeTaskFissionReward> list = weTaskFissionRewardService.selectWeTaskFissionRewardList(weTaskFissionReward);
        return getDataTable(list);
    }

    /**
     * 导出任务裂变奖励列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:reward:export')")
    @Log(title = "任务裂变奖励", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(WeTaskFissionReward weTaskFissionReward) {
        List<WeTaskFissionReward> list = weTaskFissionRewardService.selectWeTaskFissionRewardList(weTaskFissionReward);
        ExcelUtil<WeTaskFissionReward> util = new ExcelUtil<WeTaskFissionReward>(WeTaskFissionReward.class);
        return util.exportExcel(list, "reward");
    }

    /**
     * 获取任务裂变奖励详细信息
     */
    @PreAuthorize("@ss.hasPermi('wecom:reward:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(weTaskFissionRewardService.selectWeTaskFissionRewardById(id));
    }

    /**
     * 新增任务裂变奖励
     */
    @PreAuthorize("@ss.hasPermi('wecom:reward:add')")
    @Log(title = "任务裂变奖励", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WeTaskFissionReward weTaskFissionReward) {
        return toAjax(weTaskFissionRewardService.insertWeTaskFissionReward(weTaskFissionReward));
    }

    /**
     * 修改任务裂变奖励
     */
    @PreAuthorize("@ss.hasPermi('wecom:reward:edit')")
    @Log(title = "任务裂变奖励", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WeTaskFissionReward weTaskFissionReward) {
        return toAjax(weTaskFissionRewardService.updateWeTaskFissionReward(weTaskFissionReward));
    }

    /**
     * 删除任务裂变奖励
     */
    @PreAuthorize("@ss.hasPermi('wecom:reward:remove')")
    @Log(title = "任务裂变奖励", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(weTaskFissionRewardService.deleteWeTaskFissionRewardByIds(ids));
    }
}
