package com.linkwechat.wecom.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.Data;


/**
 * 实际群码对象 we_group_code_actual
 * 
 * @author ruoyi
 * @date 2020-10-07
 */
@Data
@TableName("we_group_code_actual")
public class WeGroupCodeActual
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId
    private Long id=SnowFlakeUtil.nextId();

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
