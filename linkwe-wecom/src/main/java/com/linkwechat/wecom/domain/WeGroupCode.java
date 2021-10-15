package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


/**
 * 客户群活码对象 we_group_code
 *
 * @author ruoyi
 * @date 2020-10-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("we_group_code")
public class WeGroupCode extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId
    private Long id = SnowFlakeUtil.nextId();

    /**
     * 活码URL
     */
    @ApiModelProperty(value = "活码URL")
    private String codeUrl;

    /**
     * 活码头像
     */
    @ApiModelProperty(value = "活码头像URL")
    private String avatarUrl;

    /**
     * 活码名称
     */
    @ApiModelProperty(value = "活码名称")
    @Size(max = 60, message = "活码名称最大长度为60个字符")
    @NotBlank(message = "活码名称不能为空")
    private String activityName;

    /**
     * 活码描述
     */
    @ApiModelProperty(value = "活码描述")
    @Size(max = 60, message = "活码描述最大长度为60个字符")
    private String activityDesc;

    /**
     * 场景
     */
    @ApiModelProperty(value = "场景")
    @Size(max = 60, message = "场景最大长度为60个字符")
//    @NotBlank(message = "场景不能为空")
    private String activityScene;

    /**
     * 加群引导语
     */
    @ApiModelProperty(value = "加群引导语")
    @Size(max = 100, message = "加群引导语最大长度为60个字符")
    private String guide;

    /**
     * 进群是否提示:1:是;0:否;
     */
    private Long showTip;

    /**
     * 进群提示语
     */
    @ApiModelProperty(value = "进群提示语")
    @Size(max = 100, message = "进群提示语最大长度为60个字符")
    private String tipMsg;

    /**
     * 客服二维码
     */
    @ApiModelProperty(value = "客服二维码")
    @Size(max = 100, message = "客服二维码提示语最大长度为60个字符")
    private String customerServerQrCode;

    /**
     * 0:正常; 1:删除;
     */
    @JsonIgnore
    @TableLogic(value = "0", delval = "1")
    private Long delFlag = 0L;

    /**
     * 可用实际码数量
     */
    @TableField(exist = false)
    private Long availableCodes;

    /**
     * 实际码扫码次数之和
     */
    private Long totalScanTimes=new Long(0);

    /**
     * 即将过期实际码数量
     */
    @TableField(exist = false)
    private Long aboutToExpireCodes;

    /**
     * 实际码
     */
    @TableField(exist = false)
    private List<WeGroupCodeActual> actualList;

    /**
     * 实际码id列表,用于前端绑定实际群活码
     */
    @JsonIgnore
    @TableField(exist = false)
    private List<Long> actualIdList;

}
