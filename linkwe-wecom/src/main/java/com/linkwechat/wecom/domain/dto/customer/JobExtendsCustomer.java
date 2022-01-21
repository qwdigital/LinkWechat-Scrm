package com.linkwechat.wecom.domain.dto.customer;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 在职继承客户
 */
@Data
public class JobExtendsCustomer extends WeResultDto {


    private List<ExtendsCustomer> customer;

    private String next_cursor;

    @Data
    public static class ExtendsCustomer{
        //客户id
        private String external_userid;
        //接替状态， 1-接替完毕 2-等待接替 3-客户拒绝 4-接替成员客户达到上限 5-无接替记录
        private Integer status;
        //接替客户的时间，如果是等待接替状态，则为未来的自动接替时间
        private Long takeover_time;

        private Integer errcode;

    }

    @Data
    @Builder
    public static class JobExtendsParam{
        private String handover_userid;
        private String takeover_userid;
        private String cursor;
    }

}
