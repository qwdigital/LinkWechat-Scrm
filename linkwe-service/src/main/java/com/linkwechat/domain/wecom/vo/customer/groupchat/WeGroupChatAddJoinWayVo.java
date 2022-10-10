package com.linkwechat.domain.wecom.vo.customer.groupchat;


import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 *配置客户群进群方式,返回
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WeGroupChatAddJoinWayVo extends WeResultVo {
    //联系方式的配置id
    private String config_id;
}
