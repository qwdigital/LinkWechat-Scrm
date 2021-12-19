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
     *
     * @param taskId       任务id
     * @return 客户统计列表
     */
    List<WePresTagGroupTaskStatVo> selectStatInfoByTaskId(@Param("taskId") Long taskId);

    /**
     * 通过taskId获取所有外部联系人id
     *
     * @param taskId 老客标签建群任务id
     * @return 结果
     */
    List<String> getAllExternalIdByTaskId(Long taskId);
}
