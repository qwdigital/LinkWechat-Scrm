package com.linkwechat.wecom.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WePresTagGroupTaskStat;
import com.linkwechat.wecom.domain.vo.WePresTagGroupTaskStatVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WePresTagGroupTaskStatMapper extends BaseMapper<WePresTagGroupTaskStat> {

    /**
     * 根据 老客标签建群任务id及附属相关属性条件获取任务对应的客户统计
     * @param taskId 任务id
     * @param customerName 客户名称
     * @param isInGroup 是否在群
     * @param isSent 是否已发送
     * @return 客户统计列表
     */
    List<WePresTagGroupTaskStatVo> getStatByTaskId(
            @Param("taskId") Long taskId,
            @Param("customerName") String customerName,
            @Param("isSent") Integer isSent,
            @Param("isInGroup") Integer isInGroup
    );
}
