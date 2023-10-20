package com.linkwechat.controller;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.poi.LwExcelUtil;
import com.linkwechat.domain.taggroup.WePresTagGroupTask;
import com.linkwechat.domain.taggroup.query.WePresTagGroupTaskQuery;
import com.linkwechat.domain.taggroup.vo.*;
import com.linkwechat.service.IWePresTagGroupTaskService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

//    /**
//     * 根据老客标签建群id及过滤条件，获取其统计信息
//     */
//    @GetMapping(path = "/stat/{id}")
//    public TableDataInfo<List<WePresTagGroupTaskStat>> getStatInfo(
//            @PathVariable("id") Long id,
//            @RequestParam(value = "customerName", required = false) String customerName,
//            @RequestParam(value = "isInGroup", required = false) Integer isInGroup,
//            @RequestParam(value = "isSent", required = false) Integer isSent
//    ) {
//        WePresTagGroupTask task = taskService.getById(id);
//        startPage();
//        List<WePresTagGroupTaskStat> stats = taskService.getTaskStat(id, customerName, isInGroup, isSent,
//                task.getSendType());
//        return getDataTable(stats);
//    }



}
