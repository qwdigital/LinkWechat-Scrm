package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.wecom.domain.WeKeywordGroupTask;
import com.linkwechat.wecom.service.IWeCommunityKeywordToGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 关键词拉群controller
 */
@Api(tags = "关键词拉群")
@RestController
@RequestMapping(value = "/wecom/communityKeywordGroup")
public class WeCommunityKeywordGroupController extends BaseController {

    @Autowired
    private IWeCommunityKeywordToGroupService service;


    /**
     * 根据过滤条件获取关键词拉群任务列表
     */
    @ApiOperation(value = "获取关键词拉群任务列表")
//    @PreAuthorize("@ss.hasPermi('wecom:communityKeyword:list')")
    @GetMapping(path = "/list")
    public TableDataInfo<List<WeKeywordGroupTask>> list(WeKeywordGroupTask task) {
        startPage();
        List<WeKeywordGroupTask> taskList = service.getTaskList(task);
        return getDataTable(taskList);
    }

    /**
     * 根据id获取任务详情
     *
     * @param taskId 任务id
     * @return 任务详情
     */
    @ApiOperation(value = "获取任务详情")
    //  @PreAuthorize("@ss.hasPermi('wecom:communityKeyword:query')")
    @GetMapping(path = "/{taskId}")
    public AjaxResult getTask(@ApiParam("任务id") @PathVariable("taskId") Long taskId) {
        return AjaxResult.success(service.getTaskById(taskId));
    }

    /**
     * 添加新任务
     *
     * @param task 添加任务所需的数据
     * @return 结果
     */
    @ApiOperation(value = "添加新任务")
    //   @PreAuthorize("@ss.hasPermi('wecom:communityKeyword:add')")
    @PostMapping(path = "/")
    public AjaxResult addTask(@RequestBody @Validated WeKeywordGroupTask task) {
        if (service.isNameOccupied(task)) {
            return AjaxResult.error("关键词拉群任务名称"+ task.getTaskName() +"已存在");
        }
        task.setCreateBy(SecurityUtils.getUsername());
        task.setCreateTime(new Date());
        return toAjax(service.addTask(task));
    }

    /**
     * 根据id及更新数据对指定任务进行更新
     */
    @ApiOperation(value = "更新任务")
    //   @PreAuthorize("@ss.hasPermi('wecom:communityKeyword:edit')")
    @PutMapping("/{taskId}")
    public AjaxResult updateTask(
            @ApiParam("任务id") @PathVariable("taskId") Long taskId, @RequestBody @Validated WeKeywordGroupTask task) {
        task.setTaskId(taskId);
        task.setUpdateBy(SecurityUtils.getUsername());
        task.setUpdateTime(new Date());
        return toAjax(service.updateTask(task));
    }

    /**
     * 通过id列表批量删除任务
     *
     * @param ids id列表
     * @return 结果
     */
    @ApiOperation(value = "批量删除任务")
    //   @PreAuthorize("@ss.hasPermi('wecom:communityKeyword:remove')")
    @DeleteMapping(path = "/{ids}")
    public AjaxResult batchDeleteTask(@ApiParam("待删除任务id数组") @PathVariable("ids") Long[] ids) {
        return toAjax(service.batchRemoveTaskByIds(ids));
    }

}
