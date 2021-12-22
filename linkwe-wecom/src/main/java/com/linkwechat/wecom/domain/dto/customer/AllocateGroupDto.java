package com.linkwechat.wecom.domain.dto.customer;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.Data;

import java.util.List;

@Data
public class AllocateGroupDto extends WeResultDto {

    private List<FailedChatList> failed_chat_list;


    @Data
    public static class FailedChatList{

        private String chat_id;
        private Integer errcode;
        private String errmsg;

    }

}
