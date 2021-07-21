package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeCustomerMessageOriginal;
import com.linkwechat.wecom.domain.vo.CustomerMessagePushVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 群发消息 原始数据信息表 Mapper接口
 *
 * @author kewen
 * @date 2020-12-08
 */
public interface WeCustomerMessageOriginalMapper extends BaseMapper<WeCustomerMessageOriginal> {

    /**
     * 群发消息列表
     *
     * @param sender 创建人
     * @param content 内容
     * @param pushType 群发类型
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return {@link CustomerMessagePushVo}s
     */
    public List<CustomerMessagePushVo> selectCustomerMessagePushs(@Param("sender") String sender,@Param("content") String content
            ,@Param("pushType") String pushType, @Param("beginTime") String beginTime,@Param("endTime") String endTime);

    /**
     * 群发详情
     * @param messageId 微信群发id
     * @return {@link CustomerMessagePushVo} 群发详情
     */
    public CustomerMessagePushVo findCustomerMessagePushDetail(@Param("messageId") Long messageId);

}
