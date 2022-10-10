package com.linkwechat.controller;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeTaskFission;
import com.linkwechat.domain.taskfission.query.WeAddTaskFissionQuery;
import com.linkwechat.domain.taskfission.query.WeTaskFissionStatisticQuery;
import com.linkwechat.domain.taskfission.vo.WeTaskFissionStatisticVo;
import com.linkwechat.domain.taskfission.vo.WeTaskFissionVo;
import com.linkwechat.service.IWeTaskFissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;


/**
 * 任务宝Controller
 *
 * @author leejoker
 * @date 2021-01-20
 */
@Api(tags = "任务宝管理")
@RestController
@RequestMapping("/fission")
public class WeTaskFissionController extends BaseController {

    @Autowired
    private IWeTaskFissionService weTaskFissionService;

    /**
     * 查询任务宝列表
     */
    @ApiOperation(value = "查询任务宝列表", httpMethod = "GET")
    @GetMapping("/list")
    public TableDataInfo<List<WeTaskFission>> list(WeTaskFission weTaskFission) {
        startPage();
        List<WeTaskFission> list = weTaskFissionService.selectWeTaskFissionList(weTaskFission);
        return getDataTable(list);
    }


    /**
     * 获取任务宝详细信息
     */
    @ApiOperation(value = "获取任务宝详细信息", httpMethod = "GET")
    @GetMapping(value = "/getInfo/{id}")
    public AjaxResult<WeTaskFissionVo> getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(weTaskFissionService.selectWeTaskFissionById(id));
    }

    /**
     * 新增任务宝
     */
    @ApiOperation(value = "新增任务宝", httpMethod = "POST")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody WeAddTaskFissionQuery query) {
        weTaskFissionService.insertWeTaskFission(query);
        weTaskFissionService.sendWeTaskFission(query);
        return AjaxResult.success();
    }

    /**
     * 编辑任务宝
     */
    @ApiOperation(value = "编辑任务宝", httpMethod = "PUT")
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody WeAddTaskFissionQuery query) {
        if (ObjectUtils.isEmpty(query.getId())) {
            return AjaxResult.error("数据id为空");
        }
        weTaskFissionService.updateWeTaskFission(query);
        return AjaxResult.success();
    }

    /**
     * 删除任务宝
     */
    @ApiOperation(value = "删除任务宝", httpMethod = "DELETE")
    @DeleteMapping("/delete/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        weTaskFissionService.deleteWeTaskFissionByIds(ids);
        return AjaxResult.success();
    }

    @ApiOperation(value = "查询统计信息", httpMethod = "GET")
    @GetMapping("/stat")
    public AjaxResult<WeTaskFissionStatisticVo> statistics(WeTaskFissionStatisticQuery query) throws ParseException {
        Date st;
        Date et = DateUtils.getNowDate();
        if (query.getSeven()) {
            st = DateUtils.addDays(et, -7);
        } else if (query.getThirty()) {
            st = DateUtils.addDays(et, -30);
        } else {
            if (StringUtils.isNotBlank(query.getBeginTime()) ^ StringUtils.isNotBlank(query.getEndTime())) {
                throw new WeComException("开始或结束时间不能为空");
            }
            st = DateUtils.parseDate(query.getBeginTime() + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            et = DateUtils.parseDate(query.getEndTime() + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }
        WeTaskFissionStatisticVo vo = weTaskFissionService.taskFissionStatistic(query.getTaskFissionId(), st, et);
        return AjaxResult.success(vo);
    }

}
