package com.linkwechat.wecom.mapper;

import com.linkwechat.wecom.domain.vo.WePresTagGroupTaskVo;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WePresTagGroupTask;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 老客户标签建群相关Mapper接口
 */
@Repository
public interface WePresTagGroupTaskMapper extends BaseMapper<WePresTagGroupTask> {

    /**
     * 获取老客户标签建群任务
     *
     * @param taskId 任务id
     * @return 结果
     */
    WePresTagGroupTaskVo selectTaskById(Long taskId);

    /**
     * 根据条件查询老客标签建群任务
     * @param taskName 任务名称
     * @param createBy 创建人
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param sendType 发送方式
     * @return 结果
     */
    List<WePresTagGroupTaskVo> selectTaskList(
            @Param("taskName") String taskName,
            @Param("sendType") Integer sendType,
            @Param("createBy") String createBy,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime
    );

    /**
     * 检测任务名是否已被占用
     * @param taskName 任务名
     * @return 是否被占用
     */
    int checkTaskNameUnique(String taskName);

}
