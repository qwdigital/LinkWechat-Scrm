package com.linkwechat.wecom.domain.dto.message;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.Data;

import java.util.List;

@Data
public class QueryCustomerMessageStatusResultDto extends WeResultDto {

    /**
     * 返回查询信息列表
     */
    private List<DetailMessageStatusResult> detail_list;

}

@Data
class DetailMessageStatusResult {

    /**
     * 外部联系人userid
     */
    private String external_userid;

    /**
     * 外部客户群id
     */
    private String chat_id;

    /**
     * 企业服务人员的userid
     */
    private String userid;

    /**
     * 发送状态 0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败
     */
    private String status;

    /**
     * 发送时间，发送状态为1时返回(为时间戳的形式)
     */
    private String send_time;

}