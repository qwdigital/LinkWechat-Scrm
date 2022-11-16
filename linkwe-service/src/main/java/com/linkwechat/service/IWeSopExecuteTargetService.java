package com.linkwechat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.sop.WeSopExecuteTarget;

import java.util.List;
import java.util.Set;

/**
* @author robin
* @description 针对表【we_sop_execute_target(目标执行对象表)】的数据库操作Service
* @createDate 2022-09-13 16:26:00
*/
public interface IWeSopExecuteTargetService extends IService<WeSopExecuteTarget> {

    /**
     * 非编辑sop下执行任务异常结束
     * @param executeWeUserIds
     * @param executeWeCustomerIdsOrGroupIds
     */
    void sopExceptionEnd(List<String> executeWeUserIds,List<String> executeWeCustomerIdsOrGroupIds);


    /**
     * 编辑sop时，执行任务异常结束
     * @param sopBaseId
     * @param executeWeCustomerIdsOrGroupIds
     */
    void editSopExceptionEnd(Long sopBaseId,List<String> executeWeCustomerIdsOrGroupIds);


    /**
     * 获取指定sop下满足提前结束条件设置提前结束
     * @return
     */
    void earlyEndConditionsSop();


    /**
     *  SOP日程结束后,客户或客群执行的动作
     * @param executeTargetId
     */
    void sopExecuteEndAction(Long executeTargetId);


    /**
     * 构建周期sop(目前只针对周期群sop)执行计划
     */
    void builderCycleExecutionPlan();

}
