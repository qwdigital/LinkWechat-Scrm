package com.linkwechat.wecom.domain.dto.customer;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description: 客户群相关
 * @author: HaoN
 * @create: 2020-10-20 21:52
 **/
@Data
public class CustomerGroupDetail extends WeResultDto {

    private List<GroupChat> group_chat;


    @Data
    public class GroupChat{

        /**客户群ID*/
        private String chat_id;


        /**群名*/
        private String name;

        /**群主ID*/
        private String owner;

        /**群的创建时间*/
        private long create_time;

        /**群公告*/
        private String notice;

        /**群成员列表*/
        private List<CustomerGroupMember> member_list;

    }


    /**
     * 请求参数
     */
    @Data
    public class Params{


        private String chat_id;


        public Params(String chat_id) {
            this.chat_id = chat_id;
        }
    }



}
