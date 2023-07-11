package com.linkwechat.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.UserTypes;
import com.linkwechat.common.enums.leads.template.CanEditEnum;
import com.linkwechat.common.enums.leads.template.DataAttrEnum;
import com.linkwechat.common.enums.leads.template.DatetimeTypeEnum;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.domain.leads.leads.query.WeLeadsBaseRequest;
import com.linkwechat.domain.leads.leads.vo.Properties;
import com.linkwechat.domain.leads.leads.vo.WeClientLeadsVo;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSea;
import com.linkwechat.domain.leads.template.entity.WeLeadsTemplateSettings;
import com.linkwechat.service.IWeLeadsSeaService;
import com.linkwechat.service.IWeLeadsService;
import com.linkwechat.service.IWeLeadsTemplateSettingsService;
import com.linkwechat.service.IWeTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 线索
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 14:44
 */
@Slf4j
@Api(tags = "线索相关")
@RestController
@RequestMapping("/leads")
public class WeLeadsController extends BaseController {

    @Resource
    private IWeLeadsSeaService weLeadsSeaService;
    @Resource
    private IWeLeadsService weLeadsService;
    @Resource
    private IWeTagService weTagService;
    @Resource
    private IWeLeadsTemplateSettingsService weLeadsTemplateSettingsService;

    /**
     * 线索列表
     *
     * @param request 列表请求参数
     * @return {@link TableDataInfo}
     * @author WangYX
     * @date 2023/07/11 15:17
     */
    @ApiOperation(value = "线索列表")
    @GetMapping("/list")
    public TableDataInfo managerList(WeLeadsBaseRequest request) {
        //检查公海是否存在
        checkExistSea(request.getSeaId());
        //获取列表
        startPage();
        LambdaQueryWrapper<WeLeads> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WeLeads::getSeaId, request.getSeaId());
        wrapper.eq(request.getLeadsStatus() != null, WeLeads::getLeadsStatus, request.getLeadsStatus());
        wrapper.like(StrUtil.isNotBlank(request.getName()), WeLeads::getName, request.getName());
        wrapper.like(StrUtil.isNotBlank(request.getPhone()), WeLeads::getPhone, request.getPhone());
        wrapper.orderByDesc(WeLeads::getCreateTime);
        List<WeLeads> list = weLeadsService.list(wrapper);

        TableDataInfo dataTable = getDataTable(list);
        List<WeClientLeadsVo> vos = BeanUtil.copyToList(list, WeClientLeadsVo.class);
        dataTable.setRows(vos);

        return dataTable;
    }

    /**
     * 根据公海Id获取公海数据
     *
     * @param seaId 公海Id
     * @return {@link WeLeadsSea}
     * @author WangYX
     * @date 2023/07/11 15:16
     */
    private WeLeadsSea checkExistSea(Long seaId) {
        WeLeadsSea weLeadsSea = weLeadsSeaService.getById(seaId);
        boolean exist = false;
        if (BeanUtil.isNotEmpty(weLeadsSea)) {
            if (weLeadsSea.getDelFlag().equals(Constants.COMMON_STATE)) {
                exist = true;
            }
        }
        if (!exist) {
            throw new ServiceException("不存在id为" + seaId + "的公海", HttpStatus.BAD_REQUEST);
        }
        return weLeadsSea;
    }


    /**
     * 获取线索数据
     *
     * @param id 线索Id
     * @return {@link AjaxResult< WeClientLeadsVo>}
     * @author WangYX
     * @date 2023/07/11 15:17
     */
    @GetMapping("/get/{id}")
    public AjaxResult<WeClientLeadsVo> get(@PathVariable("id") Long id) {
        WeLeads weLeads = weLeadsService.getById(id);
        if (BeanUtil.isEmpty(weLeads)) {
            throw new ServiceException("线索不存在！");
        }
        WeClientLeadsVo result = BeanUtil.copyProperties(weLeads, WeClientLeadsVo.class);

        //标签处理
        String labelsIds = result.getLabelsIds();
        String labelNames = labelsHandler(labelsIds);
        if (StrUtil.isNotBlank(labelNames)) {
            result.setLabelsNames(labelNames);
        }
        //可修改的模板属性
        LambdaQueryWrapper<WeLeadsTemplateSettings> wrapper = Wrappers.lambdaQuery(WeLeadsTemplateSettings.class);
        wrapper.eq(WeLeadsTemplateSettings::getCanEdit, CanEditEnum.ALLOW.getCode());
        wrapper.eq(WeLeadsTemplateSettings::getDelFlag, Constants.COMMON_STATE);
        List<WeLeadsTemplateSettings> list = weLeadsTemplateSettingsService.list(wrapper);
        //自定义数据处理
        String properties = result.getProperties();
        if (StrUtil.isNotBlank(properties)) {
            List<Properties> propertiesList = new ArrayList<>();
            List<Properties> tempList = JSONObject.parseArray(properties, Properties.class);
            for (WeLeadsTemplateSettings settings : list) {
                Optional<Properties> first = tempList.stream().filter(i -> i.getId().equals(settings.getId())).findFirst();
                if (first.isPresent()) {
                    Properties item = first.get();
                    //是否日期
                    if (settings.getDataAttr().equals(DataAttrEnum.DATE.getCode())) {
                        if (StrUtil.isNotBlank(item.getValue())) {
                            if (settings.getDatetimeType().equals(DatetimeTypeEnum.DATE.getCode())) {
                                //日期
                                String format = DateUtil.format(DateUtil.date(Long.valueOf(item.getValue())), DatetimeTypeEnum.DATE.getFormat());
                                item.setValue(format);
                            } else if (settings.getDatetimeType().equals(DatetimeTypeEnum.DATETIME.getCode())) {
                                //日期+时间
                                String format = DateUtil.format(DateUtil.date(Long.valueOf(item.getValue())), DatetimeTypeEnum.DATETIME.getFormat());
                                item.setValue(format);
                            }
                        }
                    }
                    item.setName(settings.getTableEntryName());
                    propertiesList.add(item);
                }
            }
            result.setPropertiesList(propertiesList);
        }
        return AjaxResult.success(result);
    }


    /**
     * 根据标签Id获取标签信息
     *
     * @param labelsIds 标签Id集合，逗号分隔
     * @return {@link String}
     * @author WangYX
     * @date 2023/07/11 15:22
     */
    private String labelsHandler(String labelsIds) {
        String labelNames = null;
        if (StrUtil.isNotBlank(labelsIds)) {
            LambdaQueryWrapper<WeTag> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(WeTag::getTagId, labelsIds.split(","));
            List<WeTag> tags = weTagService.list(queryWrapper);
            if (tags != null && tags.size() > 0) {
                List<String> collect = tags.stream().map(WeTag::getName).collect(Collectors.toList());
                labelNames = StringUtils.join(collect, ",");
            }
        }
        return labelNames;
    }


}

