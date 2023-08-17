package com.linkwechat.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.annotation.RepeatSubmit;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.SexEnums;
import com.linkwechat.common.enums.leads.leads.LeadsStatusEnum;
import com.linkwechat.common.enums.leads.record.ImportSourceTypeEnum;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.domain.leads.leads.entity.WeLeadsFollower;
import com.linkwechat.domain.leads.leads.query.*;
import com.linkwechat.domain.leads.leads.vo.Properties;
import com.linkwechat.domain.leads.leads.vo.WeLeadsImportResultVO;
import com.linkwechat.domain.leads.leads.vo.WeLeadsVO;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSea;
import com.linkwechat.service.IWeLeadsFollowerService;
import com.linkwechat.service.IWeLeadsSeaService;
import com.linkwechat.service.IWeLeadsService;
import com.linkwechat.service.IWeTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
    private IWeLeadsFollowerService weLeadsFollowerService;

    /**
     * 线索列表
     *
     * @param request 列表请求参数
     * @return {@link TableDataInfo}
     * @author WangYX
     * @date 2023/07/11 15:17
     */
    @ApiOperation(value = "线索列表")
    @GetMapping("/manager/list")
    public TableDataInfo list(WeLeadsBaseRequest request) {
        //检查公海是否存在
        checkExistSea(request.getSeaId());
        //获取列表
        startPage();
        List<WeLeadsVO> vos = weLeadsService.selectList(request);
        TableDataInfo dataTable = getDataTable(vos);
        vos.forEach(i -> {
            //手机号脱敏
            i.setPhone(this.phoneDesensitization(i.getPhone()));
            i.setSourceStr(ImportSourceTypeEnum.of(i.getSource()).getDesc());
        });
        dataTable.setRows(vos);

        return dataTable;
    }

    /**
     * 获取线索数据
     *
     * @param id 线索Id
     * @return {@link AjaxResult<   WeLeadsVO  >}
     * @author WangYX
     * @date 2023/07/11 15:17
     */
    @GetMapping("/get/{id}")
    public AjaxResult<WeLeadsVO> get(@PathVariable("id") Long id) {
        WeLeads weLeads = weLeadsService.getById(id);
        if (BeanUtil.isEmpty(weLeads)) {
            throw new ServiceException("线索不存在！");
        }
        WeLeadsVO result = BeanUtil.copyProperties(weLeads, WeLeadsVO.class);

        //标签处理
        String labelsIds = result.getLabelsIds();
        String labelNames = labelsHandler(labelsIds);
        if (StrUtil.isNotBlank(labelNames)) {
            result.setLabelsNames(labelNames);
        }
        //导入来源
        result.setSourceStr(ImportSourceTypeEnum.of(result.getSource()).getDesc());
        result.setSexStr(SexEnums.ofByCode(result.getSex()).get().getInfo());

        //前跟进人
        if (weLeads.getLeadsStatus().equals(LeadsStatusEnum.RETURNED.getCode())) {
            LambdaQueryWrapper<WeLeadsFollower> queryWrapper = Wrappers.lambdaQuery(WeLeadsFollower.class);
            queryWrapper.eq(WeLeadsFollower::getLeadsId, id);
            queryWrapper.eq(WeLeadsFollower::getLatest, 1);
            WeLeadsFollower one = weLeadsFollowerService.getOne(queryWrapper);
            if (BeanUtil.isNotEmpty(one)) {
                result.setPreFollowerName(one.getFollowerName());
            }
        }
        //自定义数据处理
        String properties = result.getProperties();
        if (StrUtil.isNotBlank(properties)) {
            List<Properties> tempList = JSONObject.parseArray(properties, Properties.class);
            result.setPropertiesList(tempList);
        }
        return AjaxResult.success(result);
    }

    /**
     * 领取线索
     *
     * @param leadsId 线索Id
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/12 14:28
     */
    @RepeatSubmit
    @GetMapping("/receive")
    public AjaxResult receiveLeads(Long leadsId) {
        weLeadsService.receiveLeads(leadsId);
        return AjaxResult.success();
    }

    /**
     * 线索分配
     *
     * @param request 线索分配请求参数
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/13 11:25
     */
    @RepeatSubmit
    @PostMapping("/allocation")
    public AjaxResult allocation(@RequestBody WeLeadsAllocationRequest request) {
        String result = weLeadsService.allocation(request);
        return AjaxResult.success(result);
    }

    /**
     * 线索导出
     *
     * @param request 导出请求参数
     * @author WangYX
     * @date 2023/07/13 11:31
     */
    @ApiOperation(value = "线索导出")
    @PostMapping("/manager/list/output")
    public void export(@RequestBody WeLeadsExportRequest request) {
        weLeadsService.export(request);
    }

    /**
     * 线索导入模板
     *
     * @author WangYX
     * @date 2023/07/13 15:33
     */
    @ApiOperation(value = "线索导入模板")
    @GetMapping("/import/template")
    public void importTemplate() {
        weLeadsService.importTemplate();
    }

    /**
     * 线索导入
     *
     * @param file  excel文件
     * @param seaId 公海Id
     * @author WangYX
     * @date 2023/07/14 11:19
     */
    @RepeatSubmit(interval = 1000)
    @ApiOperation(value = "excel批量导入")
    @PostMapping("/import/record/excel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件流对象,接收数组格式", required = true, dataType = "__File"),
            @ApiImplicitParam(name = "seaId", value = "公海id", required = true)
    })
    public AjaxResult<WeLeadsImportResultVO> importLeads(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "seaId") Long seaId) throws IOException {
        WeLeadsImportResultVO vo = weLeadsService.batchImportFromExcel(file, seaId);
        return AjaxResult.success(vo.getFeedbackMessage(),vo);
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

    /**
     * 手机号脱敏
     *
     * @param phone 手机号
     */
    private String phoneDesensitization(String phone) {
        if (StrUtil.isNotBlank(phone)) {
            phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        return phone;
    }

    /**
     * 移动端-我的跟进
     *
     * @param name   客户名称
     * @param status 线索状态 (0待分配，1跟进中，2已上门，3已退回)
     * @return {@link TableDataInfo}
     * @author WangYX
     * @date 2023/07/17 18:18
     */
    @ApiOperation(value = "移动端-我的跟进")
    @GetMapping("/my/follow")
    public TableDataInfo myFollowList(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "status") Integer status) {
        startPage();
        List<WeLeads> weLeads = weLeadsService.myFollowList(name, status);
        TableDataInfo dataTable = getDataTable(weLeads);
        List<WeLeadsVO> weLeadsVOS = BeanUtil.copyToList(weLeads, WeLeadsVO.class);
        getTagName(weLeadsVOS);
        dataTable.setRows(weLeadsVOS);
        return dataTable;
    }

    /**
     * 移动端-公海线索（默认为待分配和已退回的状态的线索）
     *
     * @param name  线索名称
     * @param seaId 公海Id
     * @return {@link TableDataInfo}
     * @author WangYX
     * @date 2023/07/18 9:56
     */
    @ApiOperation(value = "移动端-公海线索")
    @GetMapping("/sea/list")
    public TableDataInfo seaLeadsList(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "seaId") Long seaId) {
        startPage();
        List<WeLeadsVO> weLeadsVOS = weLeadsService.seaLeadsList(name, seaId);
        TableDataInfo dataTable = getDataTable(weLeadsVOS);
        getTagName(weLeadsVOS);
        return dataTable;
    }

    /**
     * 获取标签Id对应的标签名称
     *
     * @param weLeadsVOS 线索数据
     * @return
     * @author WangYX
     * @date 2023/07/18 10:51
     */
    private void getTagName(List<WeLeadsVO> weLeadsVOS) {
        List<WeTag> list = weTagService.list(Wrappers.lambdaQuery(WeTag.class).eq(WeTag::getDelFlag, Constants.COMMON_STATE));
        if (CollectionUtil.isNotEmpty(list)) {
            Map<String, String> weTagsMap = list.stream().collect(Collectors.toMap(WeTag::getTagId, WeTag::getName));
            weLeadsVOS.forEach(i -> {
                String labelsIds = i.getLabelsIds();
                if (StrUtil.isNotBlank(labelsIds)) {
                    StringBuffer labelName = new StringBuffer();
                    for (String labelsId : labelsIds.split(",")) {
                        if (StrUtil.isNotBlank(weTagsMap.get(labelsId))) {
                            if (labelName.length() != 0) {
                                labelName.append(",");
                            }
                            labelName.append(weTagsMap.get(labelsId));
                        }
                    }
                    i.setLabelsNames(labelName.toString());
                }
            });
        }
    }


    /**
     * 移动端-线索主动退回
     *
     * @param request 退回请求参数
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/18 14:06
     */
    @RepeatSubmit
    @ApiOperation(value = "移动端-线索主动退回")
    @PostMapping("/user/return")
    public AjaxResult userReturn(@RequestBody WeLeadsUserReturnRequest request) {
        weLeadsService.userReturn(request);
        return AjaxResult.success();
    }

    /**
     * 移动端-更新线索标签
     *
     * @param request 更新请求参数
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/18 14:33
     */
    @ApiOperation(value = "移动端-更新线索标签")
    @PutMapping("/update")
    public AjaxResult update(@Validated @RequestBody WeLeadsUpdateRequest request) {
        weLeadsService.update(request);
        return AjaxResult.success();
    }

    /**
     * 绑定客户
     *
     * @param
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/18 17:46
     */
    @ApiOperation(value = "移动端-绑定客户")
    @PostMapping("/bind/customer")
    public AjaxResult bindCustomer(@Validated @RequestBody WeLeadsBindCustomerRequest request) {
        weLeadsService.bindCustomer(request);
        return AjaxResult.success();
    }

    /**
     * 删除
     *
     * @param id 线索Id
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/08/16 14:26
     */
    @ApiOperation("删除")
    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable("id") Long id) {
        WeLeads weLeads = weLeadsService.getById(id);
        if (BeanUtil.isEmpty(weLeads)) {
            throw new ServiceException("线索不存在");
        }
        if (weLeads.getLeadsStatus().equals(LeadsStatusEnum.BE_FOLLOWING_UP.getCode()) || weLeads.getLeadsStatus().equals(LeadsStatusEnum.VISIT.getCode())) {
            throw new ServiceException("线索当前状态无法删除！");
        }
        LambdaUpdateWrapper<WeLeads> updateWrapper = Wrappers.lambdaUpdate(WeLeads.class);
        updateWrapper.eq(WeLeads::getId, id);
        updateWrapper.set(WeLeads::getDelFlag, Constants.DELETE_STATE);
        boolean update = weLeadsService.update(updateWrapper);
        return AjaxResult.success();
    }


}

