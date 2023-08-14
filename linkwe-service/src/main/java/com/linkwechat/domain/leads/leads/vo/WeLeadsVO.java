package com.linkwechat.domain.leads.leads.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 线索
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 15:09
 */
@Data
public class WeLeadsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
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
     * 备注标签名称
     */
    private String labelsNames;

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
    private Boolean delFlag;

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
     */
    private String returnReason;

    /**
     * 线索来源 0excel导入
     *
     * @see com.linkwechat.common.enums.leads.leads.LeadsSourceEnum 线索来源枚举
     */
    private Integer source;

    /**
     * 线索来源
     *
     * @see com.linkwechat.common.enums.leads.leads.LeadsSourceEnum 线索来源枚举
     */
    private String sourceStr;

    /**
     * @see com.linkwechat.common.enums.SexEnums
     * <p>
     * 性别, 0 = 未知, 1 = 男, 2 = 女
     */
    private Integer sex;

    /**
     * 性别
     *
     * @see com.linkwechat.common.enums.SexEnums 性别枚举
     */
    private String sexStr;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 自定义属性
     */
    private List<Properties> propertiesList;

    /**
     * 前跟进人姓名
     */
    private String preFollowerName;

    /**
     * 客户Id
     */
    private Long customerId;

    /**
     * 客户外部联系人Id
     */
    @TableField(value = "external_userid")
    private String externalUserid;


}
