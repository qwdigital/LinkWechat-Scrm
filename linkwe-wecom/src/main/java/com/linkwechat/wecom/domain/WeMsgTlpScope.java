package com.linkwechat.wecom.domain;

import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;


/**
 * 模板使用人员范围对象 we_msg_tlp_scope
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
@Data
@SuperBuilder
public class WeMsgTlpScope extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id= SnowFlakeUtil.nextId();

    /** 模板id */
    @Excel(name = "模板id")
    private Long msgTlpId;

    /** 使用人id */
    @Excel(name = "使用人id")
    private Long useUserId;

    /** 0:正常;1:删除; */
    private Integer delFlag=new Integer(0);


}
