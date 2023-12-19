package com.linkwechat.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.domain.WeKeyWordGroupSub;
import com.linkwechat.domain.community.WeKeywordGroupTask;
import com.linkwechat.service.IWeCommunityKeywordToGroupService;
import com.linkwechat.service.IWeKeyWordGroupSubService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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


    @Autowired
    private IWeKeyWordGroupSubService iWeKeyWordGroupSubService;


    /**
     * 申请构建主键
     * @return
     */
    @GetMapping("/applyToBuildPrimaryKey")
    public AjaxResult<String> applyToBuildPrimaryKey(){


        return AjaxResult.success(SnowFlakeUtil.nextId());
    }


    /**
     * 新增或更新关键词群基础信息
     * @param groupTask
     * @return
     */
    @PostMapping("/createOrUpdateBaseInfo")
    public AjaxResult<WeKeywordGroupTask> createOrUpdateBaseInfo(@RequestBody WeKeywordGroupTask groupTask) throws IOException {

        if(groupTask.getId()==null){
            return AjaxResult.error("关键词群主键不可为空");
        }
        keywordToGroupService.createOrUpdate(groupTask);
        return AjaxResult.success(groupTask);
    }


    /**
     * 点击取消时触发(避免还未新建,导致群活码创建过多占位)
     * @param groupTask
     * @return
     */
    @PostMapping("/cancelCreateBaseInfo")
    public AjaxResult cancelCreateBaseInfo(@RequestBody WeKeywordGroupTask groupTask){
        if(groupTask.getId()==null){
            return AjaxResult.error("关键词群主键不可为空");
        }
        WeKeywordGroupTask keywordGroupTask = keywordToGroupService.getById(groupTask.getId());
        if(null == keywordGroupTask){
            iWeKeyWordGroupSubService.batchRemoveWeKeyWordGroupByKeyWordId(groupTask.getId());
        }

        return AjaxResult.success();
    }


    /**
     * 获取关键词群基础信息
     * @param id
     * @return
     */
    @GetMapping("/getKeyWordGroupBaseInfo/{id}")
    public AjaxResult getKeyWordGroupBaseInfo(@PathVariable Long id){

        return AjaxResult.success(
                keywordToGroupService.getById(id)
        );
    }

    /**
     * 关键词群列表
     * @return
     */
    @GetMapping("/findWeKeyWordGroupSubs")
    public TableDataInfo<List<WeKeyWordGroupSub>> findWeKeyWordGroupSubs(WeKeyWordGroupSub keyWordGroupSub){
                startPage();
        List<WeKeyWordGroupSub> weKeyWordGroupSubs = iWeKeyWordGroupSubService.list(new LambdaQueryWrapper<WeKeyWordGroupSub>()
                .eq(WeKeyWordGroupSub::getKeywordGroupId,keyWordGroupSub.getKeywordGroupId()));

        return getDataTable(weKeyWordGroupSubs);
    }

    /**
     * 创建关键词群
     * @param weKeyWordGroupSub
     * @return
     */
    @PostMapping("/createKeyWordGroupSub")
    public AjaxResult createKeyWordGroupSub(@RequestBody WeKeyWordGroupSub weKeyWordGroupSub){

        if(weKeyWordGroupSub.getKeywordGroupId()==null){
            return AjaxResult.error("关键词群主键不可为空");
        }

        iWeKeyWordGroupSubService.createWeKeyWordGroup(weKeyWordGroupSub);

        return AjaxResult.success();
    }



    /**
     * 更新关键词群
     * @param weKeyWordGroupSub
     * @return
     */
    @PostMapping("/updateKeyWordGroupSub")
    public AjaxResult updateKeyWordGroupSub(@RequestBody WeKeyWordGroupSub weKeyWordGroupSub){


        iWeKeyWordGroupSubService.updateWeKeyWordGroup(weKeyWordGroupSub);

        return AjaxResult.success();
    }


    /**
     * 批量更新关键词群(主要针对顺序的调整)
     * @param weKeywordGroupTask
     * @return
     */
    @PostMapping("/batchUpdateKeyWordGroupSub")
    public AjaxResult batchUpdateKeyWordGroupSub(@RequestBody WeKeywordGroupTask weKeywordGroupTask){

        List<WeKeyWordGroupSub> keyWordGroupSubs = weKeywordGroupTask.getKeyWordGroupSubs();

        if(CollectionUtil.isNotEmpty(keyWordGroupSubs)){
            iWeKeyWordGroupSubService.updateBatchById(keyWordGroupSubs);
        }
        return AjaxResult.success();

    }

    /**
     * 删除客户群活码
     */
    @DeleteMapping("/batchRemoveKeyWordGroupSub/{ids}")
    public AjaxResult batchRemoveKeyWordGroupSub(@PathVariable Long[] ids) {

        iWeKeyWordGroupSubService.batchRemoveWeKeyWordGroupByIds(Arrays.asList(ids));
        return AjaxResult.success();
    }






//    /**
//     * 添加新任务
//     *
//     * @param task 添加任务所需的数据
//     * @return 结果
//     */
//    @PostMapping(path = "/")
//    public AjaxResult addTask(@RequestBody @Validated WeKeywordGroupTask task) {
//        if (keywordToGroupService.isNameOccupied(task.getTaskName())) {
//            return AjaxResult.error("关键词拉群任务名称"+ task.getTaskName() +"已存在");
//        }
//        keywordToGroupService.save(task);
//        return AjaxResult.success();
//    }

//    /**
//     * 根据id及更新数据对指定任务进行更新
//     */
//    @PutMapping("/{taskId}")
//    public AjaxResult updateTask(@PathVariable("taskId") Long taskId, @RequestBody @Validated WeKeywordGroupTask task) {
//        task.setTaskId(taskId);
//        WeKeywordGroupTask oldGroupTask = keywordToGroupService.getById(task.getTaskId());
//        if(null != oldGroupTask){
//            if(!oldGroupTask.getTaskName().equals(task.getTaskName())){
//                if (keywordToGroupService.isNameOccupied(task.getTaskName())) {
//                    return AjaxResult.error("关键词拉群任务名称"+ task.getTaskName() +"已存在");
//                }
//            }
//        }
//        keywordToGroupService.updateById(task);
//        return AjaxResult.success();
//    }

//    /**
//     * 通过id列表批量删除任务
//     *
//     * @param ids id列表
//     * @return 结果
//     */
//    @DeleteMapping(path = "/{ids}")
//    public AjaxResult batchDeleteTask(@PathVariable("ids") Long[] ids) {
//        keywordToGroupService.removeByIds(Arrays.asList(ids));
//        return AjaxResult.success();
//    }



//    /**
//     * 根据过滤条件获取关键词拉群任务列表
//     */
//    @ApiOperation(value = "获取关键词拉群任务列表")
//    @GetMapping(path = "/list")
//    public TableDataInfo<List<WeKeywordGroupTask>> list(WeKeywordGroupTask task) {
//        startPage();
//        List<WeKeywordGroupTask> taskList = keywordToGroupService.getTaskList(task);
//        return getDataTable(taskList);
//    }
//
//
//    /**
//     * 根据id获取任务详情
//     *
//     * @param taskId 任务id
//     * @return 任务详情
//     */
//    @GetMapping(path = "/{taskId}")
//    public AjaxResult<WeKeywordGroupTask> getTask(@ApiParam("任务id") @PathVariable("taskId") Long taskId) {
//        return AjaxResult.success(keywordToGroupService.getTaskById(taskId));
//    }




}
