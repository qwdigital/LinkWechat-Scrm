package com.linkwechat.wecom.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 分配客户
 * @author: HaoN
 * @create: 2020-10-24 23:26
 **/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AllocateWeCustomerDto{

    /**外部联系人的userid，注意不是企业成员的帐号*/
    private String handover_userid;

    /**原跟进成员的userid*/
    private String[] external_userid;

    /**接替成员的userid*/
    private String takeover_userid;

    /**转移成功后发给客户的消息，最多200个字符，不填则使用默认文案，目前只对在职成员分配客户的情况生效*/
    private String transfer_success_msg;



}
