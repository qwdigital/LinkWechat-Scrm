package com.linkwechat.wecom.domain.dto;

import lombok.Data;

/**
 * @description: 企业微信标签相关标签
 * @author: HaoN
 * @create: 2020-09-15 17:55
 **/
@Data
public class WeTagDto extends WeResultDto{
    private String group_name;
    private String tag_name;
    private Integer type;
    private Long flower_customer_rel_id;
}
