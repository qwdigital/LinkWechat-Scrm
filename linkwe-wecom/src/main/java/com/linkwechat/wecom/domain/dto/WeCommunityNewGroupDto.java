package com.linkwechat.wecom.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.linkwechat.wecom.domain.WeEmpleCodeTag;
import com.linkwechat.wecom.domain.WeEmpleCodeUseScop;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 社群运营 新客自动拉群
 *
 * @author kewen
 * @date 2021-02-19
 */
@ApiModel
@Data
public class WeCommunityNewGroupDto {

    /**
     * 活动场景
     */
    @ApiModelProperty("活动场景")
    private String activityScene;

    /**
     * 欢迎语
     */
    @ApiModelProperty("欢迎语")
    private String welcomeMsg;

    @NotNull(message = "员工信息不能为空")
    /** 使用员工 */
    @TableField(exist = false)
    @ApiModelProperty("使用员工")
    private List<WeEmpleCodeUseScop> weEmpleCodeUseScops;

    /**
     * 扫码标签
     */
    @TableField(exist = false)
    @ApiModelProperty("扫码标签")
    private List<WeEmpleCodeTag> weEmpleCodeTags;

    /**
     * 群活码ID
     */
    @ApiModelProperty("群活码ID")
    private Long groupCodeId;

    /**
     * 客户添加时无需经过确认自动成为好友:1:是;0:否
     */
    @ApiModelProperty("客户添加时无需经过确认自动成为好友:1:是;0:否")
    private Boolean isJoinConfirmFriends;

    /**
     * 二维码链接
     */
    @ApiModelProperty("二维码链接")
    private String qrCode;

    /**
     * 素材的id
     */
    @ApiModelProperty("素材的id")
    private Long mediaId;

}
