package com.linkwechat.wecom.domain.dto.msgaudit;

import lombok.Data;

/**
 * @author sxw
 * @description 会话存档
 * @date 2020/12/2 16:47
 **/
@Data
public class MsgauditDto {
    /**
     * 拉取对应版本的开启成员列表。1表示办公版；2表示服务版；3表示企业版。
     * 非必填，不填写的时候返回全量成员列表
     */
    private Integer type;
}
