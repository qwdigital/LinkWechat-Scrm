package com.linkwechat.wecom.domain;

import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.Data;

/**
 * 企业微信标签对象 we_tag
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
@Data
public class WeTag extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    private Long id= SnowFlakeUtil.nextId();
    /** 标签组id */
    private Long groupId;

    /** 标签名 */
    private String name;

    /** 帐号状态（0正常 1删除） */
    private String status;



}
