package com.linkwechat.wecom.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


/**
 * 实际群码对象 we_group_code_actual
 * 
 * @author ruoyi
 * @date 2020-10-07
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
@TableName("we_group_code_actual")
public class WeGroupCodeActual extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId
    private Long id=SnowFlakeUtil.nextId();

    /** 实际群码 */
    @NotNull(message = "实际群活码称不能为空")
    @ApiModelProperty("实际群码")
    private String actualGroupQrCode;

    /** 群名称 */
    @NotNull(message = "群名称不能为空")
    @ApiModelProperty("群名称")
    private String groupName;

    /** 有效期 */
    @NotNull(message = "有效期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("有效期")
    private Date effectTime;

    /** 扫码次数限制 */
    @NotNull(message = "次数限制不能为空")
    @ApiModelProperty("扫码次数限制")
    private Long scanCodeTimesLimit;

    /** 群活码id */
    @NotNull(message = "群活码id不能为空")
    @ApiModelProperty("群活码id")
    private Long groupCodeId;

    /** 客户群id */
    @ApiModelProperty("客户群id")
    private String chatId;

    /** 客户群名称 */
    @ApiModelProperty("客户群名称")
    private String chatGroupName;

    /** 扫码次数 */
    @ApiModelProperty("扫码次数")
    private Long scanCodeTimes;

    /** 0:正常使用;2:删除; */
    @ApiModelProperty("0:正常使用;2:删除;")
    private Long delFlag;

    /** 0:使用中 */
    @ApiModelProperty("0:使用中")
    private Long status;
}
