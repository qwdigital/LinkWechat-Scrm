package com.linkwechat.wecom.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WePresTagGroupTaskStat;
import com.linkwechat.wecom.domain.vo.WePresTagGroupTaskStatVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface WePresTagGroupTaskStatMapper extends BaseMapper<WePresTagGroupTaskStat> {

    int batchSave(List<WePresTagGroupTaskStat> stats);

    /**
     * 获取老客标签建群任务的消息发送结果
     * @param stat 过滤条件
     * @return 发送结果列表
     */
    List<WePresTagGroupTaskStat> cropSendResultList(WePresTagGroupTaskStat stat);

    /**
     * 获取老客标签建群任务的消息发送结果
     * @param stat 过滤条件
     * @return 发送结果列表
     */
    List<WePresTagGroupTaskStat> singleSendResultList(WePresTagGroupTaskStat stat);
}
