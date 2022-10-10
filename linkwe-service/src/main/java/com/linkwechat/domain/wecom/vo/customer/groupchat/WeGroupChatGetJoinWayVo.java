package com.linkwechat.domain.wecom.vo.customer.groupchat;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 获取客户群进群方式配置返回参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WeGroupChatGetJoinWayVo extends WeResultVo {

    //配置详情
    private JoinWay join_way;


    @Data
    public class  JoinWay{
        //新增联系方式的配置id
        private String config_id;
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
        //使用该配置的客户群ID列表
        private List<String> chat_id_list;
        //联系二维码的URL，仅在配置为群二维码时返回
        private String qr_code;
        //企业自定义的state参数，用于区分不同的入群渠道。不超过30个UTF-8字符
        private String state;

    }
}
