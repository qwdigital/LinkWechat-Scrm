package com.linkwechat.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.domain.leads.leads.entity.WeLeadsManualAddRecord;
import com.linkwechat.domain.leads.leads.query.WeLeadsAddRequest;
import com.linkwechat.domain.leads.leads.vo.WeLeadsVO;
import com.linkwechat.service.IWeLeadsManualAddRecordService;
import com.linkwechat.service.IWeLeadsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 线索手动入线记录 前端控制器
 * </p>
 *
 * @author WangYX
 * @since 2023-07-25
 */
@RestController
@RequestMapping("/leads/manual")
public class WeLeadsManualAddRecordController extends BaseController {
    @Resource
    private IWeLeadsService weLeadsService;
    @Resource
    private IWeLeadsManualAddRecordService weLeadsManualAddRecordService;

    /**
     * 手动新增线索
     *
     * @param request 请求参数
     * @return {@link AjaxResult} 线索Id
     * @author WangYX
     * @date 2023/07/25 17:40
     */
    @ApiOperation(value = "手动新增线索")
    @PostMapping("/add")
    public AjaxResult manualAdd(@RequestBody WeLeadsAddRequest request) {
        return AjaxResult.success(weLeadsManualAddRecordService.manualAdd(request));
    }

    /**
     * 手动新增线索列表
     *
     * @return {@link TableDataInfo} 分页数据
     * @author WangYX
     * @date 2023/07/25 18:11
     */
    @ApiOperation("手动新增线索列表")
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        LambdaQueryWrapper<WeLeadsManualAddRecord> queryWrapper = Wrappers.lambdaQuery(WeLeadsManualAddRecord.class);
        queryWrapper.eq(WeLeadsManualAddRecord::getWeUserId, SecurityUtils.getLoginUser().getSysUser().getWeUserId());
        List<WeLeadsManualAddRecord> list = weLeadsManualAddRecordService.list(queryWrapper);
        TableDataInfo dataTable = getDataTable(list);
        List<Long> leadsIds = list.stream().map(WeLeadsManualAddRecord::getLeadsId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(leadsIds)) {
            LambdaQueryWrapper<WeLeads> lambdaQuery = Wrappers.lambdaQuery(WeLeads.class);
            lambdaQuery.select(WeLeads::getId, WeLeads::getName, WeLeads::getPhone);
            lambdaQuery.in(WeLeads::getId, leadsIds);
            lambdaQuery.orderByDesc(WeLeads::getId);
            List<WeLeads> weLeads = weLeadsService.list(lambdaQuery);
            List<WeLeadsVO> weLeadsVOS = BeanUtil.copyToList(weLeads, WeLeadsVO.class);
            dataTable.setRows(weLeadsVOS);
        }
        return dataTable;
    }
}

