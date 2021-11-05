package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeCustomerMessageTimeTask;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 群发消息 定时任务表 Mapper接口
 *
 * @author kewen
 * @date 2021-02-09
 */
public interface WeCustomerMessageTimeTaskMapper extends BaseMapper<WeCustomerMessageTimeTask> {

    /**
     * 查询群发任务列表
     *
     * @param timeMillis 当前时间毫秒数
     * @return {@link WeCustomerMessageTimeTask}s
     */
    List<WeCustomerMessageTimeTask> selectWeCustomerMessageTimeTaskGteSettingTime(@Param("timeMillis") long timeMillis);

    /**
     * 保存群发任务
     *
     * @param customerMessageTimeTask 群发任务信息
     * @return int
     */
    int saveWeCustomerMessageTimeTask(WeCustomerMessageTimeTask customerMessageTimeTask);

    /**
     * 更新群发定时任务处理状态
     *
     * @param taskId 任务id
     * @return int
     */
    int updateTaskSolvedById(@Param("taskId") Long taskId,@Param("solved") Integer solved,@Param("exceMsg") String exceMsg);

}
