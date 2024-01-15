package com.linkwechat.controller;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.WeGroupMessageSendResult;
import com.linkwechat.domain.WeGroupMessageTask;
import com.linkwechat.domain.WeGroupMessageTemplate;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.groupmsg.vo.WeGroupMessageDetailVo;
import com.linkwechat.service.IWeGroupMessageTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 群发消息模板Controller
 *
 * @author ruoyi
 * @date 2021-10-27
 */
@RestController
@RequestMapping("/groupmsg/template")
public class WeGroupMessageTemplateController extends BaseController {

    @Autowired
    private IWeGroupMessageTemplateService iWeGroupMessageTemplateService;

    /**
     * 查询群发消息模板列表
     */
    @GetMapping("/list")
    public TableDataInfo<WeGroupMessageTemplate> list(WeGroupMessageTemplate weGroupMessageTemplate) {
        startPage();
        List<WeGroupMessageTemplate> list = iWeGroupMessageTemplateService.queryList(weGroupMessageTemplate);
        return getDataTable(list);
    }


    /**
     * 获取群发消息模板详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult<WeGroupMessageDetailVo> getGroupMsgTemplateDetail(@PathVariable("id") Long id) {
        return AjaxResult.success(iWeGroupMessageTemplateService.getGroupMsgTemplateDetail(id));
    }

    /**
     * 新增群发消息模板
     */
    @PostMapping("/add")
    public AjaxResult addGroupMsgTemplate(@RequestBody WeAddGroupMessageQuery query){
        iWeGroupMessageTemplateService.addGroupMsgTemplate(query);
        return AjaxResult.success();
    }

    /**
     * 同步消息发送结果
     */
    @GetMapping("/sync/{ids}")
    public AjaxResult sync(@PathVariable Long[] ids) {
        iWeGroupMessageTemplateService.syncGroupMsgSendResultByIds(Arrays.asList(ids));
        return AjaxResult.success();
    }

    /**
     * 取消定时发送
     */
    @GetMapping("/cancel/{ids}")
    public AjaxResult cancel(@PathVariable Long[] ids) {
        iWeGroupMessageTemplateService.cancelByIds(Arrays.asList(ids));
        return AjaxResult.success();
    }

    /**
     * 群发成员发送任务列表
     */
    @GetMapping("/task/list")
    public TableDataInfo<WeGroupMessageTask> groupMsgTaskList(WeGroupMessageTask task) {
        startPage();
        List<WeGroupMessageTask> messageTaskList = iWeGroupMessageTemplateService.groupMsgTaskList(task);
        return getDataTable(messageTaskList);
    }

    /**
     * 群发成员发送任务列表
     */
    @GetMapping("/send/result/list")
    public TableDataInfo<WeGroupMessageSendResult> groupMsgSendResultList(WeGroupMessageSendResult sendResult) {
        startPage();
        List<WeGroupMessageSendResult> sendResultList = iWeGroupMessageTemplateService.groupMsgSendResultList(sendResult);
        return getDataTable(sendResultList);
    }


    /**
     * 根据群发id获取群发内容明细数据
     * @param id
     * @return
     */
    @GetMapping("/findGroupMessageDetail/{id}")
    public AjaxResult<WeAddGroupMessageQuery> findGroupMessageDetail(@PathVariable("id") Long id){

        WeAddGroupMessageQuery groupMessageDetail = iWeGroupMessageTemplateService.findGroupMessageDetail(id);


        return AjaxResult.success(groupMessageDetail);
    }
}
