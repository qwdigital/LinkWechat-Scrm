package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
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
    private String codeUrl;

    /**
     * 二维码的uuid
     */
    private String uuid;

    /**
     * 活码头像
     */
    private String activityHeadUrl;

    /**
     * 活码名称
     */
    @NotNull(message = "活码名称不能为空")
    private String activityName;

    /**
     * 活码描述
     */
    @NotNull(message = "活码描述不能为空")
    private String activityDesc;

    /**
     * 场景
     */
    private String activityScene;

    /**
     * 加群引导语
     */
    private String guide;

    /**
     * 进群是否提示:1:是;0:否;
     */
    private Long joinGroupIsTip;

    /**
     * 进群提示语
     */
    private String tipMsg;

    /**
     * 客服二维码
     */
    private String customerServerQrCode;

    /**
     * 0:正常;2:删除;
     */
    private Long delFlag;

    /**
     * 可用实际码数量
     */
    @TableField(exist = false)
    private Long availableCodes;

    /**
     * 实际码扫码次数之和
     */
    @TableField(exist = false)
    private Long totalScanTimes;

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

}
