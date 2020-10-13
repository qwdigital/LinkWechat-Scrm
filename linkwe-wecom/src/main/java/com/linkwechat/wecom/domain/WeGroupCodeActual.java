package com.linkwechat.wecom.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;


/**
 * 实际群码对象 we_group_code_actual
 * 
 * @author ruoyi
 * @date 2020-10-07
 */
public class WeGroupCodeActual extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 实际群码 */
    private String actualGroupQrCode;

    /** 群名称 */
    private String groupName;

    /** 有效期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectTime;

    /** 扫码次数限制 */
    private Long scanCodeTimesLimit;

    /** 群活码id */
    private Long groupCodeId;

    /** 扫码次数 */
    private Long scanCodeTimes;

    /** 0:正常使用;2:删除; */
    private Long delFlag;

    /** 0:使用中 */
    private Long status;


}
