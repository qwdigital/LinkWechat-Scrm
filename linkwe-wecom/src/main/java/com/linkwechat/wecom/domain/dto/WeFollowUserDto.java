package com.linkwechat.wecom.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @description: 企业服务人员数据传输实体（配置客户联系功能的成员）
 * @author: HaoN
 * @create: 2020-09-15 14:08
 **/
@Data
public class WeFollowUserDto extends WeResultDto{

    private String[] follow_user;

    private String userid;

    private String remark;

    private String description;

    private Long createtime;

    private List<WeTagDto> tags;

    private String remark_corp_name;

    private String remark_mobiles;

    private Integer add_way;

    private String oper_userid;

    private Integer state;
}
