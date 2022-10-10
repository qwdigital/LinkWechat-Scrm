package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeGroupMember;
import com.linkwechat.domain.groupchat.vo.WeGroupMemberVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 企业微信群成员(WeGroupMember)
 *
 * @author danmo
 * @since 2022-04-02 13:35:14
 */
@Repository()
@Mapper
public interface WeGroupMemberMapper extends BaseMapper<WeGroupMember> {


    List<WeGroupMember> getPageList(WeGroupMember weGroupMember);

    void insertBatch(@Param("weGroupMembers") List<WeGroupMember> weGroupMembers);

    List<WeGroupMemberVo> selectGroupMemberListByChatId(@Param("chatId") String chatId);

    List<WeGroupMemberVo> selectGroupMemberListByChatIds(@Param("chatIds") List<String> chatIds);

    void quitGroup(@Param("quitScene") Integer quitScene,@Param("userId") String userId,@Param("chatId") String chatId);


}

