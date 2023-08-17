package com.linkwechat.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.leads.template.CanEditEnum;
import com.linkwechat.common.enums.leads.template.TableEntryAttrEnum;
import com.linkwechat.domain.leads.template.entity.WeLeadsTemplateSettings;
import com.linkwechat.domain.leads.template.entity.WeLeadsTemplateTableEntryContent;
import com.linkwechat.domain.leads.template.query.WeLeadsTemplateSettingsRequest;
import com.linkwechat.domain.leads.template.query.WeTemplateSettingsReRankRequest;
import com.linkwechat.domain.leads.template.vo.WeLeadsTemplateSettingsVO;
import com.linkwechat.domain.leads.template.vo.WeLeadsTemplateTableEntryContentVO;
import com.linkwechat.service.IWeLeadsTemplateSettingsService;
import com.linkwechat.service.IWeLeadsTemplateTableEntryContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公海模版配置表(WeLeadsTemplateSettings)表控制层
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/07 10:56
 */
@Api(tags = "公海模版配置表(WeLeadsTemplateSettings)相关")
@Validated
@RestController
@RequestMapping(value = "/leads/template/settings")
public class WeLeadsTemplateSettingsController extends BaseController {

    @Resource
    private IWeLeadsTemplateSettingsService weSeaLeadsTemplateSettingsService;
    @Resource
    private IWeLeadsTemplateTableEntryContentService weTableEntryContentService;

    /**
     * 查询全部
     *
     * @return {@link AjaxResult<List<WeLeadsTemplateSettingsVO>>}
     * @author WangYX
     * @date 2023/07/07 10:57
     */
    @ApiOperation(value = "查询全部")
    @GetMapping(value = "")
    public AjaxResult<List<WeLeadsTemplateSettingsVO>> queryAll() {
        return AjaxResult.success(weSeaLeadsTemplateSettingsService.queryAll());
    }

    /**
     * 分页
     *
     * @return {@link TableDataInfo}
     * @author WangYX
     * @date 2023/08/02 9:59
     */
    @ApiOperation(value = "分页")
    @GetMapping(value = "/page")
    public TableDataInfo page() {
        startPage();
        LambdaQueryWrapper<WeLeadsTemplateSettings> queryWrapper = Wrappers.lambdaQuery(WeLeadsTemplateSettings.class);
        queryWrapper.orderByAsc(WeLeadsTemplateSettings::getRank);
        List<WeLeadsTemplateSettings> list = weSeaLeadsTemplateSettingsService.list(queryWrapper);
        TableDataInfo dataTable = getDataTable(list);
        if (BeanUtil.isNotEmpty(list)) {
            List<WeLeadsTemplateSettingsVO> vos = BeanUtil.copyToList(list, WeLeadsTemplateSettingsVO.class);
            dataTable.setRows(vos);
        }
        return dataTable;
    }

    /**
     * 新增
     *
     * @param param 新增参数
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/07 10:57
     */
    @ApiOperation(value = "新增")
    @PostMapping(value = "")
    public AjaxResult save(@RequestBody @Validated WeLeadsTemplateSettingsRequest param) {
        return AjaxResult.success(weSeaLeadsTemplateSettingsService.saveLeadsTemplateSettings(param));
    }

    /**
     * 修改
     *
     * @param param 修改参数
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/07 10:58
     */
    @ApiOperation(value = "修改")
    @PutMapping(value = "")
    public AjaxResult update(@RequestBody @Validated WeLeadsTemplateSettingsRequest param) {
        return AjaxResult.success(weSeaLeadsTemplateSettingsService.updateLeadsTemplateSettings(param));
    }

    /**
     * 删除
     *
     * @param id 模板Id
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/07 10:58
     */
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        return AjaxResult.success(weSeaLeadsTemplateSettingsService.deleteLeadsTemplateSettings(id));
    }

    /**
     * 修改排序
     *
     * @param paramList 修改排序前端参数
     * @return {@link AjaxResult<Boolean>}
     * @author WangYX
     * @date 2023/07/07 10:58
     */
    @ApiOperation(value = "修改排序")
    @PutMapping(value = "/reRank")
    public AjaxResult<Boolean> reRank(@RequestBody List<WeTemplateSettingsReRankRequest> paramList) {
        return AjaxResult.success(weSeaLeadsTemplateSettingsService.reRank(paramList));
    }

    /**
     * 自动生成UUId
     *
     * @return {@link AjaxResult<String>}
     * @author WangYX
     * @date 2023/07/07 11:00
     */
    @ApiOperation(value = "自动生成")
    @GetMapping(value = "/auto/generate")
    public AjaxResult<String> autoGenerate() {
        return AjaxResult.success(weSeaLeadsTemplateSettingsService.autoGenerate());
    }

    /**
     * 获取可编辑的项目
     *
     * @param
     * @return {@link AjaxResult<List<WeLeadsTemplateSettingsVO>>}
     * @author WangYX
     * @date 2023/07/07 11:04
     */
    @ApiOperation(value = "获取可编辑的项目")
    @GetMapping(value = "/editable")
    public AjaxResult<List<WeLeadsTemplateSettingsVO>> editable() {
        LambdaQueryWrapper<WeLeadsTemplateSettings> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WeLeadsTemplateSettings::getCanEdit, CanEditEnum.ALLOW.getCode());
        queryWrapper.orderByAsc(WeLeadsTemplateSettings::getRank);
        List<WeLeadsTemplateSettings> list = weSeaLeadsTemplateSettingsService.list(queryWrapper);
        List<WeLeadsTemplateSettingsVO> result = list.stream().map(i -> {
            WeLeadsTemplateSettingsVO vo = BeanUtil.copyProperties(i, WeLeadsTemplateSettingsVO.class);
            if (vo.getTableEntryAttr().equals(TableEntryAttrEnum.COMBOBOX.getCode())) {
                List<WeLeadsTemplateTableEntryContent> contents = weTableEntryContentService.getByLeadsTemplateSettingsId(vo.getId());
                if (CollectionUtil.isNotEmpty(contents)) {
                    List<WeLeadsTemplateTableEntryContentVO> contentVOS = BeanUtil.copyToList(contents, WeLeadsTemplateTableEntryContentVO.class);
                    vo.setTableEntryContent(contentVOS);
                }
            }
            return vo;
        }).collect(Collectors.toList());
        return AjaxResult.success(result);
    }

    /**
     * 详情
     *
     * @param id 主键Id
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/08/02 10:22
     */
    @ApiOperation("详情")
    @GetMapping("/{id}")
    public AjaxResult get(@PathVariable("id") Long id) {
        return AjaxResult.success(weSeaLeadsTemplateSettingsService.get(id));
    }
}

