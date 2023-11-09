package com.linkwechat.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.poi.LwExcelUtil;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.community.WeCommunityNewGroup;
import com.linkwechat.domain.community.query.WeCommunityNewGroupQuery;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.taggroup.WePresTagGroupTask;
import com.linkwechat.domain.taggroup.query.WePresTagGroupTaskQuery;
import com.linkwechat.domain.taggroup.vo.*;
import com.linkwechat.service.IWeGroupService;
import com.linkwechat.service.IWePresTagGroupTaskService;
import com.linkwechat.service.IWeTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 老客标签建群接口
 */
@RestController
@RequestMapping(value = "/communityPresTagGroup")
public class WeCommunityPresTagGroupController extends BaseController {

    @Autowired
    private IWePresTagGroupTaskService taskService;

    @Autowired
    private IWeGroupService iWeGroupService;


    @Autowired
    private IWeTagService iWeTagService;


    /**
     * 获取老客标签建群列表数据
     */
    @GetMapping(path = "/list")
    public TableDataInfo<List<WePresTagGroupTask>> list(WePresTagGroupTask groupTask) {
        startPage();
        List<WePresTagGroupTask> result = taskService.selectTaskList(groupTask);
        return getDataTable(result);
    }

    /**
     * 新建老客标签建群任务
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody @Validated WePresTagGroupTask task) {
         taskService.add(task);
        return AjaxResult.success();
    }

    /**
     * 获取根据id详情
     */
    @GetMapping(path = "/{id}")
    public AjaxResult<WePresTagGroupTask> findWePresTagGroupById(@PathVariable("id") Long id) {
        WePresTagGroupTask taskVo = taskService.getTaskById(id);
        if(null != taskVo){
            WeCustomersQuery weCustomersQuery = taskVo.getWeCustomersQuery();
            if(null != weCustomersQuery && StringUtils.isNotEmpty(weCustomersQuery.getTagIds())){
                List<WeTag> weTags = iWeTagService.list(new LambdaQueryWrapper<WeTag>()
                        .in(WeTag::getTagId, weCustomersQuery.getTagIds().split(",")));
                if(CollectionUtil.isNotEmpty(weTags)){
                    weCustomersQuery.setTagNames(
                            weTags.stream().map(WeTag::getName).collect(Collectors.joining(","))
                    );
                }
            }
        }

        return AjaxResult.success(taskVo);
    }

    /**
     * 更新任务信息
     */
    @PutMapping(path = "/edit")
    public AjaxResult edit(@RequestBody WePresTagGroupTask task) {

        taskService.updateTask(task);

        return AjaxResult.success();
    }

    /**
     * 批量删除老客标签建群任务
     */
    @DeleteMapping(path = "/batchRemove/{ids}")
    public AjaxResult batchRemove(@PathVariable("ids") Long[] ids) {
        taskService.batchRemoveTaskByIds(ids);
        return AjaxResult.success();
    }



    /**
     * 获取头部统计
     * @param id
     * @return
     */
    @GetMapping("/countTab/{id}")
    public AjaxResult<WePresTagGroupTaskTabCountVo> countTab(@PathVariable String id){
        return AjaxResult.success(
                taskService.countTab(id)
        );
    }


    /**
     * 数据趋势
     * @param task
     * @return
     */
    @GetMapping("/findTrendCountVo")
    public AjaxResult<List<WePresTagGroupTaskTrendCountVo>> findTrendCountVo(WePresTagGroupTask task){
        return AjaxResult.success(
                taskService.findTrendCountVo(task)
        );
    }


    /**
     * 数据明细
     * @param wePresTagGroupTaskQuery
     * @return
     */
    @GetMapping("/findWePresTagGroupTaskTable")
    public TableDataInfo<List<WePresTagGroupTaskTableVo>> findWePresTagGroupTaskTable(WePresTagGroupTaskQuery wePresTagGroupTaskQuery){
        startPage();

        return getDataTable(
                taskService.findWePresTagGroupTaskTable(wePresTagGroupTaskQuery)
        );
    }


    /**
     * 数据明细导出
     */
    @GetMapping("/exprotWePresTagGroupTaskTable")
    public void exprotWePresTagGroupTaskTable(){
        LwExcelUtil.exprotForWeb(
                ServletUtils.getResponse(), WePresTagGroupTaskTableVo.class,
                taskService.findWePresTagGroupTaskTable(new WePresTagGroupTaskQuery())
                ,"老客建群-数据明细"
        );
    }



    /**
     * 同步发送结果
     * @param id
     * @return
     */
    @GetMapping("/synchExecuteResult/{id}")
    public AjaxResult synchExecuteResult(@PathVariable String id){
        taskService.synchExecuteResult(id);

        return AjaxResult.success();

    }

    /**
     * 获取当前客户对应的群
     * @param wePresTagGroupTaskQuery
     * @return
     */
    @GetMapping("/findWeCommunityNewGroupChatTable")
    public TableDataInfo<WeGroup> findWeCommunityNewGroupChatTable(WePresTagGroupTaskQuery wePresTagGroupTaskQuery){
        List<WeGroup> weGroups =new ArrayList<>();
        WePresTagGroupTask wePresTagGroupTask = taskService.getById(wePresTagGroupTaskQuery.getId());
        if(null != wePresTagGroupTask){
            startPage();
            weGroups=iWeGroupService
                    .findGroupByUserId(wePresTagGroupTaskQuery.getExternalUserid()
                            , wePresTagGroupTask.getGroupCodeState());
        }

        return getDataTable(weGroups);
    }







}
