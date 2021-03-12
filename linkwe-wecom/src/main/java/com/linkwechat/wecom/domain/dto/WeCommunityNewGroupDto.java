package com.linkwechat.wecom.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.linkwechat.wecom.domain.WeEmpleCodeTag;
import com.linkwechat.wecom.domain.WeEmpleCodeUseScop;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import javax.validation.constraints.NotNull;
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
     * 主键
     */
    private String newGroupId;

    /**
     * 活动场景
     */
    private String activityScene;

    /**
     * 欢迎语
     */
    private String welcomeMsg;

    @NotNull(message = "员工信息不能为空")
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

    /**
     * 新增联系方式的配置id
     */
    private String configId;

    /**
     * 二维码链接
     */
    private String qrCode;

    /**
     * 素材的id
     */
    private Long mediaId;

}
