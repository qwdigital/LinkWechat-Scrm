package com.linkwechat.wecom.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 分配客户
 * @author: HaoN
 * @create: 2020-10-24 23:26
 **/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AllocateWeCustomerDto extends WeResultDto{

    /**外部联系人的userid，注意不是企业成员的帐号*/
    private String handover_userid;

    /**原跟进成员的userid*/
    private String[] external_userid;

    /**接替成员的userid*/
    private String takeover_userid;

    /**转移成功后发给客户的消息，最多200个字符，不填则使用默认文案，目前只对在职成员分配客户的情况生效*/
    private String transfer_success_msg;



    private List<Info> info;


    //是否是最后一条记录
    private Boolean is_last;

    private String next_cursor;



    @Data
    @Builder
    public static class CheckParm{

        private Integer page_id;
        private Integer page_size;
        private String cursor;
    }

    @Data
    public static class  Info{
        //离职成员的userid
        private String handover_userid;
        //外部联系人userid
        private String external_userid;
        //成员离职时间
        private Long dimission_time;

    }





}
