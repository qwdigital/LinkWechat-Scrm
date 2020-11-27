package com.linkwechat.wecom.domain.dto.message;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.Data;

import java.util.List;

@Data
public class SendMessageResultDto extends WeResultDto {

    /**
     * 无效或无法发送的external_userid列表
     */
    private List<String> fail_list;

    /**
     * 企业群发消息的id，可用于<a href="https://work.weixin.qq.com/api/doc/90000/90135/92136">获取群发消息发送结果</a>
     */
    private String msgid;

}
