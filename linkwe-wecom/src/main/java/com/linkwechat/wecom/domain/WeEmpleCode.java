package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.util.List;


/**
 * 员工活码对象 we_emple_code
 *
 * @author ruoyi
 * @date 2020-10-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("we_emple_code")
public class WeEmpleCode extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId
    private Long id = SnowFlakeUtil.nextId();

    /**
     * 活码类型:1:单人;2:多人;3:批量;
     */
    private Integer codeType;


    /**
     * 活动场景,和 联系我 接口需要的 scene 不是一回事
     */
    private String scenario;

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
    private Integer delFlag = 0;

    /** 使用员工 */
    @NotEmpty(message = "员工信息不能为空")
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
     * 企业自定义的state参数，用于区分客户具体是通过哪个「联系我」添加
     */
    private String state;

    /**
     * 扫码次数
     */
    private Integer scanTimes = 0;

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

    /**
     * 客户添加时是否需要经过确认自动成为好友  1:是  0:否
     */
    private Integer isJoinConfirmFriends;
}
