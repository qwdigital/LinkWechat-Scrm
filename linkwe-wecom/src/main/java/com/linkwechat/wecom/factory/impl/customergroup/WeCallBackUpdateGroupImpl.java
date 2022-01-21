package com.linkwechat.wecom.factory.impl.customergroup;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.linkwechat.common.enums.CallbackEventUpdateDetail;
import com.linkwechat.wecom.domain.callback.WeBackBaseVo;
import com.linkwechat.wecom.domain.callback.WeBackCustomerGroupVo;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 客户群变更事件
 * @date 2021/1/20 0:39
 **/
@Slf4j
@Component("update")
public class WeCallBackUpdateGroupImpl extends WeEventStrategy {
    @Autowired
    private IWeGroupService weGroupService;

    @Autowired
    private IWeGroupCodeActualService groupCodeActualService;

    @Autowired
    private IWeTaskFissionService taskFissionService;

    @Autowired
    private IWeTaskFissionRecordService taskFissionRecordService;

    @Autowired
    private IWeTaskFissionCompleteRecordService taskFissionCompleteRecordService;

    @Autowired
    private IWeGroupMemberService groupMemberService;



    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackCustomerGroupVo customerGroupInfo = (WeBackCustomerGroupVo) message;
        try {
            weGroupService.updateWeGroup(customerGroupInfo.getChatId());
            String updateDetail = customerGroupInfo.getUpdateDetail();
            if (updateDetail.equals(CallbackEventUpdateDetail.ADD_MEMBER.getType())) { //成员入群
                // 添加成员，该群的实际群活码扫码次数需要加1
                groupCodeActualService.updateScanTimesByChatId(customerGroupInfo.getChatId());
                ThreadUtil.execAsync(() ->{
                    groupFissionEnterCheck(customerGroupInfo.getChatId());
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("update>>>>>>>>>param:{},ex:{}",customerGroupInfo.getChatId(),e);
        }
    }

    /**
     * 群裂变客户入群校验
     * @param chatId
     */
    private void groupFissionEnterCheck(String chatId){
        log.info("群裂变客户入群校验>>>>>>>>>>>>>>>>>>>>>start");
        WeGroupCodeActual weGroupCodeActual = groupCodeActualService.selectActualCodeByChatId(chatId);
        //查询群活码id
        Long groupCodeId = Optional.ofNullable(weGroupCodeActual).map(WeGroupCodeActual::getGroupCodeId).orElse(0L);
        //根据群活码id 查询任务列表
        List<WeTaskFission> taskFissionList =  taskFissionService.getTaskFissionListByGroupCodeId(groupCodeId);
        if(CollectionUtil.isNotEmpty(taskFissionList)){
            Set<Long> taskFissionIdSet = taskFissionList.stream().map(WeTaskFission::getId).collect(Collectors.toSet());
            Map<Long, Long> taskFissionIdNumMap = taskFissionList.stream().collect(Collectors.toMap(WeTaskFission::getId, WeTaskFission::getFissNum));
            //根据任务id查询裂变任务完成无效哦记录列表
            List<WeTaskFissionCompleteRecord> completeRecordList = taskFissionCompleteRecordService.getListByTaskIds(taskFissionIdSet);
            if(CollectionUtil.isNotEmpty(completeRecordList)){
                completeRecordList.forEach(completeRecord ->{
                    //查询该成员是否入群
                    WeGroupMember weGroupMember = groupMemberService.selectWeGroupMemberByUnionId(chatId,completeRecord.getCustomerId());
                    if (weGroupMember != null){
                        completeRecord.setStatus(0);
                        if(taskFissionCompleteRecordService.updateWeTaskFissionCompleteRecord(completeRecord) > 0){
                            WeTaskFissionRecord record = taskFissionRecordService.selectWeTaskFissionRecordById(completeRecord.getFissionRecordId());
                            if(record != null){
                                Long fissNum = record.getFissNum() + 1;
                                if (fissNum >= taskFissionIdNumMap.get(record.getTaskFissionId())){
                                    record.setCompleteTime(new Date());
                                }
                                record.setFissNum(fissNum);
                                taskFissionRecordService.updateWeTaskFissionRecord(record);
                            }
                        }
                    }
                });
            }
        }
    }
}
