package com.linkwechat.domain.leads.leads.entity;

import com.baomidou.mybatisplus.annotation.TableId;
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
     * 备注
     */
    private String notes;

    /**
     * 初诊时间
     */
    private String firstVisit;

    /**
     * 预约时间
     */
    private Date appointmentTime;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 留电时间
     */
    private Date leavePhoneTime;

    /**
     * 链接
     */
    private String link;

    /**
     * 一级渠道
     */
    private Long primaryChannel;

    /**
     * 二级渠道
     */
    private Long secondaryChannel;

    /**
     * 三级渠道
     */
    private Long threeLevelChannel;

    /**
     * 主题
     */
    private String subject;

    /**
     * 线索状态(0待分配，1跟进中，2已上门，3已退回，4线索转移)
     */
    private Integer leadsStatus;

    /**
     * 线索跟进中的子状态。从we_leads_pending_status表中取值
     */
    private String leadsPendingStatus;

    /**
     * 亲属关系
     */
    private String kinship;

    /**
     * 次级关系
     */
    @Deprecated
    private String secondaryRelation;

    /**
     * 就近院区
     */
    private Long nearbyArea;

    /**
     * 引流员工工号
     */
    private String userJobNumber;

    /**
     * 引流员工姓名
     */
    private String userJobName;



    /**
     * 备注标签
     */
    private String labelsIds;

    /**
     * 微信状态（0验证中，1不存在，2加微信已通过）
     */
    private Integer weixinStatus;

    /**
     * 客户微信号
     */
    private String weixinId;

    /**
     * 咨询项目
     */
    private String consult;

    /**
     * 所属公海
     */
    private Long seaId;

    /**
     * 自定义属性
     */
    private String properties;

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
     */
    private Integer source;

    /**
     * @see com.linkwechat.common.enums.SexEnums
     * <p>
     * 性别, 0 = 未知, 1 = 男, 2 = 女
     */
    private Integer sex;
}
