package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.wecom.domain.WeMaterial;
import com.linkwechat.wecom.domain.dto.ResetCategoryDto;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import com.linkwechat.wecom.domain.vo.WeMaterialFileVO;
import com.linkwechat.wecom.service.IWeMaterialService;
import com.linkwechat.wecom.service.IWePosterService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private IWePosterService wePosterService;

    /**
     * 查询素材列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:material:list')")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "categoryId", required = false) String categoryId
            , @RequestParam(value = "search", required = false) String search,@RequestParam(value = "mediaType") String mediaType) {
        startPage();
        List<WeMaterial> list;
        if(StringUtils.isNotBlank(mediaType) && mediaType.equals(MediaType.POSTER.getType())){
            list = wePosterService.list(StringUtils.isBlank(categoryId)?null:Long.valueOf(categoryId),search).stream().map(wePoster -> {
                WeMaterial weMaterial = new WeMaterial();
                weMaterial.setMaterialName(wePoster.getTitle());
                weMaterial.setMaterialUrl(wePoster.getSampleImgPath());
                weMaterial.setCategoryId(wePoster.getCategoryId());
                weMaterial.setId(wePoster.getId());
                return weMaterial;
            }).collect(Collectors.toList());
        }else {
            list = materialService.findWeMaterials(categoryId, search,mediaType);
        }

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
    @PostMapping("/upload")
    @ApiOperation("上传素材信息")
    public AjaxResult upload(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "mediaType") String mediaType) {
        WeMaterialFileVO weMaterialFileVO = materialService.uploadWeMaterialFile(file, mediaType);
        return AjaxResult.success(weMaterialFileVO);
    }

    /**
     * 更换分组
     */
    @PreAuthorize("@ss.hasPermi('wechat:material:resetCategory')")
    @Log(title = "更换分组", businessType = BusinessType.OTHER)
    @PutMapping("/resetCategory")
    @ApiOperation("更换分组")
    public AjaxResult resetCategory(@RequestBody ResetCategoryDto resetCategoryDto) {
        materialService.resetCategory(resetCategoryDto.getCategoryId(), resetCategoryDto.getMaterials());
        return AjaxResult.success();
    }

    //@PreAuthorize("@ss.hasPermi('wechat:material:temporaryMaterialMediaId')")
    @Log(title = "获取素材media_id", businessType = BusinessType.OTHER)
    @GetMapping("/temporaryMaterialMediaId")
    @ApiOperation("获取素材media_id")
    public AjaxResult temporaryMaterialMediaId(@RequestParam(value = "url") String url, @RequestParam(value = "type") String type,@RequestParam(value = "name") String name){
        WeMediaDto weMediaDto = materialService.uploadTemporaryMaterial(url, type,name);
        return AjaxResult.success(weMediaDto);
    }

}
