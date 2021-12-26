package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.CommunityTaskType;
import com.linkwechat.system.service.ISysUserService;
import com.linkwechat.wecom.domain.WeKeywordGroupTask;
import com.linkwechat.wecom.service.IWeCommunityKeywordToGroupService;
import com.linkwechat.wecom.service.IWeGroupSopService;
import com.linkwechat.wecom.service.IWePresTagGroupTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 社区运营H5接口
 *
 * @Author Hang
 * @Date 2021/3/24 10:54
 */
@RestController
@RequestMapping(value = "/wecom/community/h5")
public class WeCommunityH5Controller extends BaseController {

    @Autowired
    IWePresTagGroupTaskService tagGroupTaskService;

    @Autowired
    IWeGroupSopService sopService;

    @Autowired
    IWeCommunityKeywordToGroupService keywordToGroupService;

    @Autowired
    ISysUserService userService;

    /**
     * 获取任务对应的执行人列表
     * @param taskId 任务id
     * @param type 任务类型 1：标签建群任务 2：sop任务
     * @return
     */
    @GetMapping("/scope/{taskId}")
    public AjaxResult getTaskScopeList(@PathVariable("taskId") Long taskId, @RequestParam(value = "type") Integer type) {
        if (type.equals(CommunityTaskType.TAG.getType())) {
            return AjaxResult.success(tagGroupTaskService.getScopeListByTaskId(taskId));
        } else {
            return AjaxResult.success(sopService.getScopeListByRuleId(taskId));
        }
    }

    /**
     * h5页面根据员工id获取老客标签建群和群sop任务信息
     *
     * @param emplId 员工id
     * @param type   数据类型，0:全部数据 1:老客标签建群数据 2:群SOP数据
     * @return
     */
    @GetMapping("/{emplId}")
    public AjaxResult getEmplTask(@PathVariable("emplId") String emplId, @RequestParam(value = "type") Integer type) {
        AjaxResult res = AjaxResult.success();
        if (type.equals(CommunityTaskType.TAG.getType())) {
            // 老客标签建群数据
            res.put("todo", tagGroupTaskService.getFollowerTaskList(emplId, 0));
            res.put("done", tagGroupTaskService.getFollowerTaskList(emplId, 1));
        } else if (type.equals(CommunityTaskType.SOP.getType())) {
            // 群SOP数据
            res.put("todo", sopService.getEmplTaskList(emplId, false));
            res.put("done", sopService.getEmplTaskList(emplId, true));
        } else {
            // 全部数据
            List todoList = new ArrayList();
            List doneList = new ArrayList();
            todoList.addAll(tagGroupTaskService.getFollowerTaskList(emplId, 0));
            todoList.addAll(sopService.getEmplTaskList(emplId, false));
            res.put("todo", todoList);
            doneList.addAll(tagGroupTaskService.getFollowerTaskList(emplId, 1));
            doneList.addAll(sopService.getEmplTaskList(emplId, true));
            res.put("done", doneList);
        }
        SysUser user = userService.selectUserByUserName(emplId);
        boolean isAdmin = user != null && user.isAdmin();
        res.put("isAdmin", isAdmin);
        return res;
    }

    /**
     * 员工发送老客标签建群任务信息或者发送sop到其客户群之后，变更其任务状态
     *
     * @param taskId 老客标签建群时代表任务id，sop时，代表规则id
     * @param emplId 老客标签建群时代表员工id，sop时，代表群主
     * @param type   类型 0：老客标签建群 1：sop
     * @return 结果
     */
    @GetMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestParam("taskId") Long taskId, @RequestParam("emplId") String emplId, @RequestParam("type") Integer type) {
        if (type.equals(0)) {

            return toAjax(tagGroupTaskService.updateFollowerTaskStatus(taskId, emplId));
        } else {
            return toAjax(sopService.updateChatSopStatus(taskId, emplId));
        }
    }

    /**
     * 用于支持H5页面的名称和关键字检索
     *
     * @param word 过滤字符
     * @return 结果
     */
    @GetMapping(path = "/filter")
    public TableDataInfo filter(@RequestParam("word") String word) {
        startPage();
        List<WeKeywordGroupTask> taskList = keywordToGroupService.filterByNameOrKeyword(word);
        return getDataTable(taskList);
    }
}
