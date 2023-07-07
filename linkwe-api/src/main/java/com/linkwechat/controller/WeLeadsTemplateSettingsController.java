package com.linkwechat.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.core.domain.AjaxResult;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
public class WeLeadsTemplateSettingsController {

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
     * 新增
     *
     * @param param 新增参数
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/07 10:57
     */
    @ApiOperation(value = "新增")
    @PutMapping(value = "")
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
    @PostMapping(value = "")
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
    @PostMapping(value = "/reRank")
    public AjaxResult<Boolean> reRank(@RequestBody List<WeTemplateSettingsReRankRequest> paramList) {
        return AjaxResult.success(weSeaLeadsTemplateSettingsService.reRank(paramList));
    }

    /**
     * 导出模版表格
     *
     * @param response
     * @author WangYX
     * @date 2023/07/07 10:59
     */
    @ApiOperation(value = "导出模版表格")
    @GetMapping(value = "/outPut/template/excel")
    public void outPutTemplateExcel(HttpServletResponse response) throws IOException {
        weSeaLeadsTemplateSettingsService.outPutTemplateExcel(response);
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
        List<WeLeadsTemplateSettings> list = weSeaLeadsTemplateSettingsService.list(queryWrapper);
        List<WeLeadsTemplateSettingsVO> result = list.stream().map(i -> {
            WeLeadsTemplateSettingsVO vo = BeanUtil.copyProperties(i, WeLeadsTemplateSettingsVO.class);
            if (vo.getDataAttr().equals(TableEntryAttrEnum.COMBOBOX.getCode())) {
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


    //--------------------------------------------------------------------------------------//
    //下面为创略具体的业务，后面参考时候需要，不需要时移除


    @ApiOperation(value = "获取咨询项目列表")
    @GetMapping(value = "/getConsultSelectItem")
    public AjaxResult<List<String>> getConsultSelectItem() {
        List<WeLeadsTemplateTableEntryContent> consultSelectItem = weSeaLeadsTemplateSettingsService.getConsultSelectItem();
        List<String> consultList = consultSelectItem.stream().map(WeLeadsTemplateTableEntryContent::getContent).collect(Collectors.toList());
        return AjaxResult.success(consultList);
    }

    @ApiOperation(value = "获取年龄下拉框列表")
    @GetMapping(value = "/getAgeSelectItem")
    public AjaxResult<List<String>> getAgeSelectItem() {
        List<WeLeadsTemplateTableEntryContent> ageSelectItem = weSeaLeadsTemplateSettingsService.getAgeSelectItem();
        List<String> ageSelectList = ageSelectItem.stream().map(WeLeadsTemplateTableEntryContent::getContent).collect(Collectors.toList());
        return AjaxResult.success(ageSelectList);
    }

    @ApiOperation(value = "获取亲属关系的枚举值")
    @GetMapping(value = "/getKinshipSelectItem")
    public AjaxResult<List<String>> getKinshipSelectItem() {
        List<WeLeadsTemplateTableEntryContent> kinshipSelectItem = weSeaLeadsTemplateSettingsService.getKinshipSelectItem();
        List<String> kinshipList = kinshipSelectItem.stream().map(WeLeadsTemplateTableEntryContent::getContent).collect(Collectors.toList());
        return AjaxResult.success(kinshipList);
    }


}

