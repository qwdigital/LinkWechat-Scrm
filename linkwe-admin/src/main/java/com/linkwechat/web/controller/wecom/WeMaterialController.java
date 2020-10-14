package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.WeMaterial;
import com.linkwechat.wecom.domain.vo.WeMaterialFileVO;
import com.linkwechat.wecom.service.IWeMaterialService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 企业微信素材Controller
 *
 * @author KEWEN
 * @date 2020-10-08
 */
@RestController
@RequestMapping("/wecom/material")
public class WeMaterialController extends BaseController {


    @Autowired
    private IWeMaterialService materialService;

    /**
     * 查询素材列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:material:list')")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "categoryId", required = false) String categoryId, @RequestParam(value = "search", required = false) String search) {
        startPage();
        List<WeMaterial> list = materialService.findWeMaterials(categoryId, search);
        return getDataTable(list);
    }

    /**
     * 查询素材详细信息
     */
    @PreAuthorize("@ss.hasPermi('wechat:material:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("查询素材详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(materialService.findWeMaterialById(id));
    }

    /**
     * 添加素材信息
     */
    @PreAuthorize("@ss.hasPermi('wechat:material:add')")
    @Log(title = "添加素材信息", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("添加素材信息")
    public AjaxResult add(@RequestBody WeMaterial material) {
        return toAjax(materialService.insertWeMaterial(material));
    }

    /**
     * 更新素材信息
     */
    @PreAuthorize("@ss.hasPermi('wechat:material:edit')")
    @Log(title = "更新素材信息", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("更新素材信息")
    public AjaxResult edit(@RequestBody WeMaterial material) {
        return toAjax(materialService.updateWeMaterial(material));
    }

    /**
     * 删除素材信息
     */
    @PreAuthorize("@ss.hasPermi('wechat:material:remove')")
    @Log(title = "删除素材信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation("删除素材信息")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(materialService.deleteWeMaterialByIds(ids));
    }

    /**
     * 上传素材信息
     */
    @PreAuthorize("@ss.hasPermi('wechat:material:upload')")
    @Log(title = "上传素材信息", businessType = BusinessType.OTHER)
    @PostMapping
    @ApiOperation("上传素材信息")
    public AjaxResult upload(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "type") String type) {
        WeMaterialFileVO weMaterialFileVO = materialService.uploadWeMaterialFile(file, type);
        return AjaxResult.success(weMaterialFileVO);
    }

}
