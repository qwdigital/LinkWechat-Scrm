package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.community.WeGroupSopChat;
import com.linkwechat.domain.community.vo.WeCommunityTaskEmplVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 群sop和群聊关联对象 mapper接口
 */
@Mapper
@Repository
public interface WeGroupSopChatMapper extends BaseMapper<WeGroupSopChat> {

    /**
     * 批量保存sop - chat 绑定
     *
     * @param sopChatList 待绑定对象
     * @return 结果
     */
    int batchBindsSopChat(List<WeGroupSopChat> sopChatList);


    /**
     * 根据SOP 规则id获取所有使用人员信息
     *
     * @param ruleId sop id
     * @return 结果
     */
    List<WeCommunityTaskEmplVo> getScopeListByRuleId(Long ruleId);

}
