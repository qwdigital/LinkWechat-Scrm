package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.wecom.domain.WeSensitiveAct;
import com.linkwechat.wecom.domain.WeSensitiveActHit;
import com.linkwechat.wecom.service.IWeSensitiveActHitService;
import com.linkwechat.wecom.service.IWeSensitiveActService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 敏感行为管理接口
 *
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/1/12 18:07
 */
@RestController
@RequestMapping("/wecom/sensitive/act")
public class WeSensitiveActController extends BaseController {
    @Autowired
    private IWeSensitiveActService weSensitiveActService;

    @Autowired
    private IWeSensitiveActHitService weSensitiveActHitService;

    /**
     * 查询敏感行为列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:sensitiveact:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeSensitiveAct weSensitiveAct) {
        startPage();
        List<WeSensitiveAct> list = weSensitiveActService.selectWeSensitiveActList(weSensitiveAct);
        return getDataTable(list);
    }

    /**
     * 获取敏感行为详细信息
     */
    @PreAuthorize("@ss.hasPermi('wecom:sensitiveact:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(weSensitiveActService.selectWeSensitiveActById(id));
    }

    /**
     * 新增敏感行为设置
     */
    @PreAuthorize("@ss.hasPermi('wecom:sensitiveact:add')")
    @Log(title = "新增敏感行为", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody WeSensitiveAct weSensitiveAct) {
        return weSensitiveActService.insertWeSensitiveAct(weSensitiveAct) ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 修改敏感词设置
     */
    @PreAuthorize("@ss.hasPermi('wecom:sensitiveact:edit')")
    @Log(title = "修改敏感行为", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WeSensitiveAct weSensitiveAct) {
        Long id = weSensitiveAct.getId();
        WeSensitiveAct originData = weSensitiveActService.selectWeSensitiveActById(id);
        if (originData == null) {
            return AjaxResult.error(HttpStatus.NOT_FOUND, "数据不存在");
        }
        return weSensitiveActService.updateWeSensitiveAct(weSensitiveAct) ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 删除敏感词设置
     */
    @PreAuthorize("@ss.hasPermi('wecom:sensitiveact:remove')")
    @Log(title = "删除敏感行为", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") String ids) {
        String[] id = ids.split(",");
        Long[] idArray = new Long[id.length];
        Arrays.stream(id).map(Long::parseLong).collect(Collectors.toList()).toArray(idArray);
        return weSensitiveActService.deleteWeSensitiveActByIds(idArray) ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 敏感词命中查询
     */
    @PreAuthorize("@ss.hasPermi('wecom:sensitiveacthit:list')")
    @GetMapping("/hit/list")
    public TableDataInfo hitList() {
        startPage();
        List<WeSensitiveActHit> list = weSensitiveActHitService.list();
        return getDataTable(list);
    }

    /**
     * 导出敏感行为记录
     */
    @PreAuthorize("@ss.hasPermi('wecom:sensitiveacthit:export')")
    @PostMapping("/hit/export")
    public AjaxResult export() {
        List<WeSensitiveActHit> list = weSensitiveActHitService.list();
        ExcelUtil<WeSensitiveActHit> util = new ExcelUtil<>(WeSensitiveActHit.class);
        return util.exportExcel(list, "敏感行为记录");
    }
}
