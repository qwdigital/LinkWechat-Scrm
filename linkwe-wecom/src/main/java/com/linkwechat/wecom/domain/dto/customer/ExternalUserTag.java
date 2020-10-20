package com.linkwechat.wecom.domain.dto.customer;

import lombok.Data;

/**
 * @description: 外部联系人标签
 * @author: HaoN
 * @create: 2020-10-19 23:35
 **/
@Data
public class ExternalUserTag {
    /**该成员添加此外部联系人所打标签的分组名称*/
    private String group_name;
    /**该成员添加此外部联系人所打标签名称*/
    private String tag_name;
    /** 该成员添加此外部联系人所打标签类型, 1-企业设置, 2-用户自定义*/
    private Integer type;
    /** 该成员添加此外部联系人所打企业标签的id，仅企业设置（type为1）的标签返回*/
    private String tag_id;
}
