package com.linkwechat.wecom.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @description: 客户实体
 * @author: HaoN
 * @create: 2020-09-15 17:43
 **/
@Data
public class WeCustomerDto extends WeResultDto{

    private String[] external_userid;


    private ExternalContact external_contact;


    private List<WeFollowUserDto> follow_user;


    @Data
    class ExternalContact{
        private String external_userid;
        private String name;
        private String position;
        private String avatar;
        private String corp_name;
        private String corp_full_name;
        private Integer type;
        private Integer gender;
        private String unionid;

    }

    @Data
    public class WeCustomerRemark {
        private String userid;
        private String external_userid;
        private String remark;
        private String description;
        private String remark_company;
        private String[] remark_mobiles;
        private String remark_pic_mediaid;
    }


}
