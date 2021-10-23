package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeGroupMessageList;
import com.linkwechat.wecom.domain.vo.WeGroupMessageDetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 群发消息列Mapper接口
 *
 * @author ruoyi
 * @date 2021-10-19
 */
public interface WeGroupMessageListMapper extends BaseMapper<WeGroupMessageList> {


    /**
     * 查询群发消息详情
     * @param msgIds
     * @return
     */
    List<WeGroupMessageDetailVo> getGroupMsgDetails(@Param("msgIds") List<String> msgIds);

    WeGroupMessageDetailVo getGroupMsgDetail(@Param("msgId") String msgId);
}
