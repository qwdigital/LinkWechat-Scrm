package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeGroupSopChat;
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
}
