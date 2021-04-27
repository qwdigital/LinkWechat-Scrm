package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.wecom.domain.WeKeywordGroupTask;
import com.linkwechat.wecom.domain.dto.WeKeywordGroupTaskDto;
import com.linkwechat.wecom.domain.vo.WeKeywordGroupTaskVo;
import com.linkwechat.wecom.service.IWeCommunityKeywordToGroupService;
import com.linkwechat.wecom.service.IWeGroupCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 关键词拉群controller
 */
@RestController
@RequestMapping(value = "/wecom/communityKeywordGroup")
public class WeCommunityKeywordGroupController extends BaseController {

    @Autowired
    private IWeCommunityKeywordToGroupService keywordToGroupService;

    @Autowired
    private IWeGroupCodeService groupCodeService;

    /**
     * 根据过滤条件获取关键词拉群任务列表
     *
     * @param taskName  任务名称
     * @param createBy  创建人
     * @param keyword   关键词
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 列表数据
     */
//    @PreAuthorize("@ss.hasPermi('wecom:communityKeyword:list')")
    @GetMapping(path = "/list")
    public TableDataInfo<List<WeKeywordGroupTaskVo>> list(
            @RequestParam(value = "taskName", required = false) String taskName,
            @RequestParam(value = "createBy", required = false) String createBy,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime
    ) {
        startPage();
        List<WeKeywordGroupTaskVo> taskList = keywordToGroupService
                .getTaskList(taskName, createBy, keyword, beginTime, endTime);
        return getDataTable(taskList);
    }

    /**
     * 根据id获取任务详情
     *
     * @param taskId 任务id
     * @return 任务详情
     */
    //  @PreAuthorize("@ss.hasPermi('wecom:communityKeyword:query')")
    @GetMapping(path = "/{taskId}")
    public AjaxResult getTask(@PathVariable("taskId") Long taskId) {
        return AjaxResult.success(keywordToGroupService.getTaskById(taskId));
    }

    /**
     * 添加新任务
     *
     * @param keywordToGroupDto 添加任务所需的数据
     * @return 结果
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:communityKeyword:add')")
    @PostMapping(path = "/")
    public AjaxResult addTask(@RequestBody @Validated WeKeywordGroupTaskDto keywordToGroupDto) {
        return toAjax(keywordToGroupService.addTask(keywordToGroupDto));
    }

    /**
     * 根据id及更新数据对指定任务进行更新
     *
     * @param taskId            任务id
     * @param keywordToGroupDto 更新所需数据
     * @return 结果
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:communityKeyword:edit')")
    @PutMapping(path = "/{taskId}")
    public AjaxResult updateTask(@PathVariable("taskId") Long taskId, @RequestBody @Validated WeKeywordGroupTaskDto keywordToGroupDto) {
        return toAjax(keywordToGroupService.updateTask(taskId, keywordToGroupDto));
    }

    /**
     * 通过id列表批量删除任务
     *
     * @param ids id列表
     * @return 结果
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:communityKeyword:remove')")
    @DeleteMapping(path = "/{ids}")
    public AjaxResult batchDeleteTask(@PathVariable("ids") Long[] ids) {
        return toAjax(keywordToGroupService.batchRemoveTaskByIds(ids));
    }

}
