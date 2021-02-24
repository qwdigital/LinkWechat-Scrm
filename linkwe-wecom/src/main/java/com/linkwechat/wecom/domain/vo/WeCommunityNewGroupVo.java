package com.linkwechat.wecom.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.wecom.domain.WeEmpleCodeTag;
import com.linkwechat.wecom.domain.WeEmpleCodeUseScop;
import com.linkwechat.wecom.domain.WeGroupCodeActual;
import com.linkwechat.wecom.domain.WeMaterial;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 社区运营 新客自动拉群
 */
@Data
public class WeCommunityNewGroupVo {

    /**
     *主键ID
     */
    private Long newGroupId;

    /**
     * 员工活码名称
     */
    private String empleCodeName;

    /**
     * 活动场景
     */
    private String activityScene;

    /**
     * 欢迎语
     */
    private String welcomeMsg;

    /**
     * 新增联系方式的配置id
     */
    private String configId;

    /**
     * 二维码链接
     */
    private String qrCode;

    /**
     * 客户添加时无需经过确认自动成为好友:1:是;0:否
     */
    private Boolean isJoinConfirmFriends;

    /**
     * 添加好友数
     */
    private Integer joinFriendNums;

    @NotEmpty(message = "员工信息不能为空")
    /** 使用员工 */
    @TableField(exist = false)
    private List<WeEmpleCodeUseScop> weEmpleCodeUseScops;

    /**
     * 扫码标签
     */
    @TableField(exist = false)
    private List<WeEmpleCodeTag> weEmpleCodeTags;

    /**
     * 扫码标签
     */
    @TableField(exist = false)
    private WeMaterial weMaterial;

    /**
     * 素材的id
     */
    private Long mediaId;

    /**
     * 实际群聊
     */
    private List<WeGroupCodeActual> weGroupUserScops;

    /** 创建者 */
    @ApiModelProperty(hidden = true)
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(hidden = true)
    private Date createTime=new Date();

}
