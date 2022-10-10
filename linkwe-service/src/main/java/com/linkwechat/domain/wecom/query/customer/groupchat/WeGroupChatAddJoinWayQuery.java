package com.linkwechat.domain.wecom.query.customer.groupchat;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.*;

import java.util.List;

/**
 * 配置客户群进群方式参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeGroupChatAddJoinWayQuery extends WeBaseQuery {

    //场景。1 - 群的小程序插件;2 - 群的二维码插件
    private Integer scene;
    //联系方式的备注信息，用于助记，超过30个字符将被截断
    private String remark;
    //当群满了后，是否自动新建群。0-否；1-是。 默认为1
    private Integer auto_create_room;
    //自动建群的群名前缀，当auto_create_room为1时有效。最长40个utf8字符
    private String room_base_name;
    //自动建群的群起始序号，当auto_create_room为1时有效
    private Integer room_base_id;
    //使用该配置的客户群ID列表，支持5个
    private List<String> chat_id_list;
    //企业自定义的state参数，用于区分不同的入群渠道。不超过30个UTF-8字符
    private String state;
}
