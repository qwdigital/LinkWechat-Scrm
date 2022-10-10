package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.community.WeGroupSop;
import com.linkwechat.domain.community.vo.WeCommunityTaskEmplVo;
import com.linkwechat.domain.community.vo.WeGroupSopVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeGroupSopMapper extends BaseMapper<WeGroupSop> {
    /**
     * 通过规则id获取其所有群聊id
     *
     * @param ruleId sop规则id
     * @return 结果
     */
    List<String> getChatIdListByRuleId(Long ruleId);


    /**
     * 通过规则id获取其所有素材id
     *
     * @param ruleId sop规则id
     * @return 结果
     */
    List<Long> getMaterialIdListByRuleId(Long ruleId);

    /**
     * 根据过滤条件获取群sop规则列表
     *
     * @param ruleName  规则名称
     * @param createBy  创建人
     * @param beginTime 创建开始时间
     * @param endTime   创建结束时间
     * @return 结果
     */
    @DataScope(type = "2", value = @DataColumn(name = "create_by_id", userid = "user_id"))
    List<WeGroupSopVo> getGroupSopList(
            @Param("ruleName") String ruleName,
            @Param("createBy") String createBy,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime
    );


    /**
     *  根据员工id获取对应的sop任务列表
     * @param emplId 员工id
     * @param isDone 已完成还是待处理
     * @return 结果
     */
    List<WeGroupSopVo> getEmplTaskList(@Param("emplId") String emplId, @Param("isDone") boolean isDone);



    /**
     * 变更某员工sop规则发送任务的状态
     *
     * @param ruleId 规则名称
     * @param emplId 员工id(即群聊群主)
     * @return 结果
     */
    int updateChatSopStatus(@Param("ruleId") Long ruleId, @Param("emplId") String emplId);


    /**
     * 根据SOP 规则id获取所有使用人员信息
     *
     * @param ruleId sop id
     * @return 结果
     */
    List<WeCommunityTaskEmplVo> getScopeListByRuleId(@Param("ruleId") Long ruleId);
}
