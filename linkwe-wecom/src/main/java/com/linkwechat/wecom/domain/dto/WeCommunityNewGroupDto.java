package com.linkwechat.wecom.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.linkwechat.wecom.domain.WeEmpleCodeTag;
import com.linkwechat.wecom.domain.WeEmpleCodeUseScop;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 社群运营 新客自动拉群
 *
 * @author kewen
 * @date 2021-02-19
 */
@Data
public class WeCommunityNewGroupDto {

    /**
     * 活动场景
     */
    private String activityScene;

    /**
     * 欢迎语
     */
    private String welcomeMsg;

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
     * 群活码ID
     */
    private Long groupCodeId;

    /**
     * 客户添加时无需经过确认自动成为好友:1:是;0:否
     */
    private Boolean isJoinConfirmFriends;

}
