package com.linkwechat.controller;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.community.WeKeywordGroupTask;
import com.linkwechat.service.IWeCommunityKeywordToGroupService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;


/**
 * 关键词拉群
 */
@RestController
@RequestMapping(value = "/communityKeywordGroup")
public class WeCommunityKeywordGroupController extends BaseController {

    @Autowired
    private IWeCommunityKeywordToGroupService keywordToGroupService;

    /**
     * 添加新任务
     *
     * @param task 添加任务所需的数据
     * @return 结果
     */
    @PostMapping(path = "/")
    public AjaxResult addTask(@RequestBody @Validated WeKeywordGroupTask task) {
        if (keywordToGroupService.isNameOccupied(task.getTaskName())) {
            return AjaxResult.error("关键词拉群任务名称"+ task.getTaskName() +"已存在");
        }
        keywordToGroupService.save(task);
        return AjaxResult.success();
    }

    /**
     * 根据id及更新数据对指定任务进行更新
     */
    @PutMapping("/{taskId}")
    public AjaxResult updateTask(@PathVariable("taskId") Long taskId, @RequestBody @Validated WeKeywordGroupTask task) {
        task.setTaskId(taskId);
        WeKeywordGroupTask oldGroupTask = keywordToGroupService.getById(task.getTaskId());
        if(null != oldGroupTask){
            if(!oldGroupTask.getTaskName().equals(task.getTaskName())){
                if (keywordToGroupService.isNameOccupied(task.getTaskName())) {
                    return AjaxResult.error("关键词拉群任务名称"+ task.getTaskName() +"已存在");
                }
            }
        }
        keywordToGroupService.updateById(task);
        return AjaxResult.success();
    }

    /**
     * 通过id列表批量删除任务
     *
     * @param ids id列表
     * @return 结果
     */
    @DeleteMapping(path = "/{ids}")
    public AjaxResult batchDeleteTask(@PathVariable("ids") Long[] ids) {
        keywordToGroupService.removeByIds(Arrays.asList(ids));
        return AjaxResult.success();
    }



    /**
     * 根据过滤条件获取关键词拉群任务列表
     */
    @ApiOperation(value = "获取关键词拉群任务列表")
    @GetMapping(path = "/list")
    public TableDataInfo<List<WeKeywordGroupTask>> list(WeKeywordGroupTask task) {
        startPage();
        List<WeKeywordGroupTask> taskList = keywordToGroupService.getTaskList(task);
        return getDataTable(taskList);
    }


    /**
     * 根据id获取任务详情
     *
     * @param taskId 任务id
     * @return 任务详情
     */
    @GetMapping(path = "/{taskId}")
    public AjaxResult<WeKeywordGroupTask> getTask(@ApiParam("任务id") @PathVariable("taskId") Long taskId) {
        return AjaxResult.success(keywordToGroupService.getTaskById(taskId));
    }




}
