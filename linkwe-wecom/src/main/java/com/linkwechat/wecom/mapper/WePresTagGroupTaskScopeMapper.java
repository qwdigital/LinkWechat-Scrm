package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WePresTagGroupTaskScope;
import com.linkwechat.wecom.domain.vo.WeCommunityTaskEmplVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WePresTagGroupTaskScopeMapper extends BaseMapper<WePresTagGroupTaskScope> {

    /**
     * 根据建群任务id获取所有使用人员信息
     *
     * @param taskId 建群任务id
     * @return 结果
     */
    List<WeCommunityTaskEmplVo> getScopeListByTaskId(Long taskId);

    /**
     * 批量绑定任务与使用人员
     * @param taskScopeList 待绑定对象
     * @return 结果
     */
    int batchBindsTaskScopes(List<WePresTagGroupTaskScope> taskScopeList);

}
