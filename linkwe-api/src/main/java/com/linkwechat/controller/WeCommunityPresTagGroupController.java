package com.linkwechat.controller;

import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.taggroup.WePresTagGroupTask;
import com.linkwechat.domain.taggroup.WePresTagGroupTaskStat;
import com.linkwechat.domain.taggroup.vo.WePresTagGroupTaskVo;
import com.linkwechat.domain.taggroup.vo.WePresTagTaskListVo;
import com.linkwechat.service.IWePresTagGroupTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api(tags = "老客标签建群接口")
@RestController
@RequestMapping(value = "/communityPresTagGroup")
public class WeCommunityPresTagGroupController extends BaseController {

    @Autowired
    private IWePresTagGroupTaskService taskService;


    /**
     * 获取老客标签建群列表数据
     */
    //  @PreAuthorize("@ss.hasPermi('wecom:communitytagGroup:list')")
    @GetMapping(path = "/list")
    @ApiOperation(value = "获取老客标签建群任务分页数据", httpMethod = "GET")
    public TableDataInfo<List<WePresTagTaskListVo>> getPage(
            @RequestParam(value = "taskName", required = false) String taskName,
            @RequestParam(value = "sendType", required = false) Integer sendType,
            @RequestParam(value = "createBy", required = false) String createBy,
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime) {
        startPage();
        List<WePresTagTaskListVo> result = taskService.selectTaskList(taskName, sendType, createBy, beginTime, endTime);
        return getDataTable(result);
    }

    /**
     * 新建老客标签建群任务
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:communitytagGroup:add')")
    @PostMapping
    @ApiOperation(value = "新建老客标签建群任务", httpMethod = "POST")
    public AjaxResult add(@RequestBody @Validated WePresTagGroupTask task) {
        // 检测任务名是否可用
        if (taskService.isNameOccupied(task)) {
            return AjaxResult.error("任务名已存在");
        }
         taskService.add(task);
        return AjaxResult.success();
    }

    /**
     * 根据获取任务详细信息
     */
    //  @PreAuthorize("@ss.hasPermi('wecom:communitytagGroup:query')")
    @GetMapping(path = "/{id}")
    @ApiOperation(value = "根据获取任务详细信息", httpMethod = "GET")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        WePresTagGroupTaskVo taskVo = taskService.getTaskById(id);
        if (StringUtils.isNull(taskVo)) {
            return AjaxResult.error(HttpStatus.NOT_FOUND, "群活码不存在");
        }
        return AjaxResult.success(taskVo);
    }

    /**
     * 更新任务信息
     */
    //    @PreAuthorize("@ss.hasPermi('wecom:communitytagGroup:edit')")
    @PutMapping(path = "/{id}")
    @ApiOperation(value = "更新任务信息", httpMethod = "PUT")
    public AjaxResult update(@PathVariable("id") Long id, @RequestBody @Validated WePresTagGroupTask task) {

        try {
            // 保存新任务
            task.setTaskId(id);
            task.setUpdateTime(new Date());
            taskService.updateTaskAndSendMsg(task);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }

        return AjaxResult.success();
    }

    /**
     * 批量删除老客标签建群任务
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:communitytagGroup:remove')")
    @DeleteMapping(path = "/{ids}")
    @ApiOperation(value = "批量删除老客标签建群任务", httpMethod = "DELETE")
    public AjaxResult batchRemove(@PathVariable("ids") Long[] ids) {
        return AjaxResult.success(taskService.batchRemoveTaskByIds(ids));
    }

    /**
     * 根据老客标签建群id及过滤条件，获取其统计信息
     */
    @GetMapping(path = "/stat/{id}")
    public TableDataInfo<List<WePresTagGroupTaskStat>> getStatInfo(
            @PathVariable("id") Long id,
            @RequestParam(value = "customerName", required = false) String customerName,
            @RequestParam(value = "isInGroup", required = false) Integer isInGroup,
            @RequestParam(value = "isSent", required = false) Integer isSent
    ) {
        WePresTagGroupTask task = taskService.getById(id);
        startPage();
        List<WePresTagGroupTaskStat> stats = taskService.getTaskStat(id, customerName, isInGroup, isSent,
                task.getSendType());
        return getDataTable(stats);
    }
}
