package com.linkwechat.service.impl.strategic.state;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.TaskFissionType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeTaskFission;
import com.linkwechat.domain.WeTaskFissionRecord;
import com.linkwechat.domain.strategic.crowd.query.WeCorpStateTagSourceQuery;
import com.linkwechat.service.IWeStrategicCrowdStateTagService;
import com.linkwechat.service.IWeTaskFissionRecordService;
import com.linkwechat.service.IWeTaskFissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QwActiveFissionImpl extends IWeStrategicCrowdStateTagService {

    @Autowired
    private IWeTaskFissionService fissionService;

    @Autowired
    private IWeTaskFissionRecordService weTaskFissionRecordService;

    @Override
    public List<Map<String, Object>> getStateTagSourceList(WeCorpStateTagSourceQuery query) {
        LambdaQueryWrapper<WeTaskFission> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(query.getName()), WeTaskFission::getTaskName, query.getName());
        wrapper.eq(WeTaskFission::getFissionType, 1);
        wrapper.eq(WeTaskFission::getDelFlag, 0);
        List<WeTaskFission> taskFissionList = fissionService.list(wrapper);
        if (CollectionUtil.isNotEmpty(taskFissionList)) {
            List<Map<String, Object>> taskTagList = taskFissionList.parallelStream().map(item -> {
                Map<String, Object> map = new HashMap<>();
                map.put("code", item.getId());
                TaskFissionType taskFissionType = TaskFissionType.parseEnum(item.getFissionType());
                if(taskFissionType != null){
                    map.put("value", item.getTaskName() + "-" + taskFissionType.getValue());
                }else {
                    map.put("value", item.getTaskName());
                }
                return map;
            }).collect(Collectors.toList());
            return taskTagList;
        }
        return new ArrayList<>();
    }

    @Override
    public List<WeCustomer> getStateTagCustomerList(String code) {
        WeTaskFissionRecord query = WeTaskFissionRecord.builder().taskFissionId(Long.valueOf(code)).build();
        List<WeTaskFissionRecord> fissionRecordList = weTaskFissionRecordService.selectWeTaskFissionRecordList(query);
        //获取当前任务相关的渠道值
        List<String> fissionStateList = Optional.ofNullable(fissionRecordList).orElseGet(ArrayList::new).parallelStream()
                .map(item -> {
                    return WeConstans.FISSION_PREFIX + item.getId();
                }).collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(fissionStateList)){
            return weCustomerService.list(new LambdaQueryWrapper<WeCustomer>()
                    .in(WeCustomer::getState, fissionStateList).eq(WeCustomer::getDelFlag,0));
        }else {
            return new ArrayList<>();
        }
    }
}
