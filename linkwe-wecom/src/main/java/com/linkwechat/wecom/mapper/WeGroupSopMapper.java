package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeGroupSop;
import com.linkwechat.wecom.domain.vo.WeGroupSopVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 群SOP mapper接口
 */
@Mapper
@Repository
public interface WeGroupSopMapper extends BaseMapper<WeGroupSop> {

    /**
     * 根据过滤条件获取群sop规则列表
     *
     * @param ruleName  规则名称
     * @param createBy  创建人
     * @param beginTime 创建开始时间
     * @param endTime   创建结束时间
     * @return 结果
     */
    List<WeGroupSopVo> getGroupSopList(
            @Param("ruleName") String ruleName,
            @Param("createBy") String createBy,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime
    );

    /**
     * 通过id获取sop规则
     *
     * @param sopId id
     * @return 结果
     */
    WeGroupSopVo getGroupSopById(Long sopId);

    /**
     * 批量删除群sop
     *
     * @param ruleIds 群sop规则id列表
     * @return 结果
     */
    int batchRemoveWeGroupSopByIds(Long[] ruleIds);

    /**
     * 通过规则id获取其所有素材id
     *
     * @param ruleId sop规则id
     * @return 结果
     */
    List<Long> getMaterialIdListByRuleId(Long ruleId);

    /**
     * 通过规则id获取其所有群聊id
     *
     * @param ruleId sop规则id
     * @return 结果
     */
    List<String> getChatIdListByRuleId(Long ruleId);

    /**
     *  根据员工id获取对应的sop任务列表
     * @param emplId 员工id
     * @param isDone 已完成还是待处理
     * @return 结果
     */
    List<WeGroupSopVo> getEmplTaskList(@Param("emplId") String emplId, @Param("isDone") boolean isDone);

}
