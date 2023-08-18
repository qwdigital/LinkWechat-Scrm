package com.linkwechat.domain.wecom.vo.customer.link;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;

import java.util.List;


@Data
public class WeLinkWecustomerCountVo extends WeResultVo {


    private List<CustomerList> customer_list;



    @Data
    public static class CustomerList{

        //客户external_userid
        private String external_userid;

        //通过获客链接添加此客户的跟进人userid
        private String userid;

        //会话状态，0-客户未发消息 1-客户已发送消息
        private Integer chat_status;

        private String state;

    }
}
