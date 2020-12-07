package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;


/**
 * 员工活码对象 we_emple_code
 *
 * @author ruoyi
 * @date 2020-10-04
 */
@Data
@TableName("we_emple_code")
public class WeEmpleCode extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id = SnowFlakeUtil.nextId();

    /**
     * 活码类型:1:单人;2:多人;3:批量;
     */
    private Integer codeType;

    /**
     * 客户添加时无需经过确认自动成为好友:1:是;0:否
     */
    private Boolean isJoinConfirmFriends;

    /**
     * 活动场景
     */
    private String activityScene;

    /**
     * 欢迎语
     */
    private String welcomeMsg;

    /**
     * 素材的id
     */
    private Long mediaId;

    /**
     * 0:正常;1:删除;
     */
    private Integer delFlag = new Integer(0);

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
     * 新增联系方式的配置id
     */
    private String configId;

    /**
     * 二维码链接
     */
    private String qrCode;

    /**
     * 使用员工姓名
     */
    @TableField(exist = false)
    private String useUserName;

    /**
     * 使用员工手机号
     */
    @TableField(exist = false)
    private String mobile;
}
