package com.linkwechat.wecom.domain;

import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.Data;

import java.util.List;

/**
 * 标签组对象 we_tag_group
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
@Data
public class WeTagGroup extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id= SnowFlakeUtil.nextId();

    /** 分组名称 */
    private String gourpName;

    /** 标签 */
    private List<WeTag> weTags;

    /** 帐号状态（0正常 1删除） */
    private String status;
}
