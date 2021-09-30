package com.linkwechat.wecom.domain.dto.customer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.AllArgsConstructor;
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

    private List<GroupChat> groupChat;


    @Data
    public class GroupChat{

        /**客户群ID*/
        private String chatId;


        /**群名*/
        private String name;

        /**群主ID*/
        private String owner;

        /**群的创建时间*/
        private long createTime;

        /**群公告*/
        private String notice;

        /**群成员列表*/
        private List<CustomerGroupMember> memberList;

        /**群管理员列表*/
        private List<JSONObject> adminList;

    }


    /**
     * 请求参数
     */
    @AllArgsConstructor
    @Data
    public class Params{


        private String chat_id;

        private Integer need_name;

    }



}
