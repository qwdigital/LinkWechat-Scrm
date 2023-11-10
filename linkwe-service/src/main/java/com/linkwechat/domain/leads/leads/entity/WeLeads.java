package com.linkwechat.domain.leads.leads.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 线索
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 14:25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("we_leads")
public class WeLeads extends BaseEntity {

    /**
     * 主键Id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * @see com.linkwechat.common.enums.leads.leads.LeadsStatusEnum
     * 线索状态(0待分配，1跟进中，2已上门，3已退回)
     */
    private Integer leadsStatus;

    /**
     * 备注标签
     */
    private String labelsIds;

    /**
     * 自定义属性
     */
    private String properties;

    /**
     * 所属公海
     */
    private Long seaId;

    /**
     * 删除标识
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 当前跟进人Id
     */
    private Long followerId;

    /**
     * 当前跟进人Id
     */
    private String weUserId;

    /**
     * 跟进人
     */
    private String followerName;

    /**
     * 当前跟进人部门Id
     */
    private String deptId;

    /**
     * 回收次数
     */
    private Integer recoveryTimes;

    /**
     * 末次回收原因
     *
     * @see com.linkwechat.common.enums.leads.record.FollowBackModeEnum
     */
    private Integer returnReason;

    /**
     * 线索来源
     *
     * @see com.linkwechat.common.enums.leads.record.ImportSourceTypeEnum
     */
    private Integer source;

    /**
     * 性别 0 = 未知, 1 = 男, 2 = 女
     *
     * @see com.linkwechat.common.enums.SexEnums
     */
    private Integer sex;

    /**
     * 导入记录Id
     */
    private Long importRecordId;

    /**
     * 客户Id
     */
    private Long customerId;

    /**
     * 客户外部联系人Id
     */
    @TableField(value = "external_userid")
    private String externalUserid;

    /**
     * 绑定客户时间
     */
    @TableField(value = "bind_customer_time")
    private Date bindCustomerTime;


}
