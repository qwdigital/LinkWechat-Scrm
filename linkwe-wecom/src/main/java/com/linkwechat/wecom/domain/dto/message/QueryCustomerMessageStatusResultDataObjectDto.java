package com.linkwechat.wecom.domain.dto.message;

import lombok.Data;

@Data
public class QueryCustomerMessageStatusResultDataObjectDto {
    /**
     * 群发消息的id，通过<a href=“https://work.weixin.qq.com/api/doc/90000/90135/92135”>创建企业群发接口</a>返回
     */
    private String msgid;




}
