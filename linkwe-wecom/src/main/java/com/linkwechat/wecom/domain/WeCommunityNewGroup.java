package com.linkwechat.wecom.domain;

import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 社群运营 新客自动拉群
 *
 * @author kewen
 * @date 2021-02-19
 */
@ApiModel
@Data
public class WeCommunityNewGroup extends BaseEntity {

    /**
     *主键ID
     */
    private Long newGroupId= SnowFlakeUtil.nextId();

    /**
     * 员工活码名称
     */
    @ApiModelProperty("员工活码名称")
    private String empleCodeName;

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

    /**
     * 二维码链接
     */
    @ApiModelProperty("二维码链接")
    private String qrCode;

    /**
     * 客户添加时无需经过确认自动成为好友:1:是;0:否
     */
    @ApiModelProperty("客户添加时无需经过确认自动成为好友:1:是;0:否")
    private Boolean isJoinConfirmFriends;

    /**
     * 添加好友数
     */
    @ApiModelProperty("添加好友数")
    private Integer joinFriendNums;

    /**
     * 群活码ID
     */
    @ApiModelProperty("群活码ID")
    private Long groupCodeId;

    /**
     * 素材的id
     */
    @ApiModelProperty("素材的id")
    private Long mediaId;

    /**
     * 0 未删除 1 已删除
     */
    @ApiModelProperty("0 未删除 1 已删除")
    private int delFlag;

}
