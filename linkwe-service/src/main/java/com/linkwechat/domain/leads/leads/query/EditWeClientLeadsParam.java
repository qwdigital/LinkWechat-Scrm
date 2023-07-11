package com.linkwechat.domain.leads.leads.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.leads.vo.WeClientLeadsVo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 线索
 *
 * @author WangYX
 * @since 2023-04-04
 */
@Data
public class EditWeClientLeadsParam {

    /**
     * 主键Id
     */
    @NotNull(message = "主键Id必填")
    private Long id;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名必填")
    private String name;

    /**
     * 电话号码
     */
    @NotBlank(message = "电话号码必填")
    private String phone;

    /**
     * 年龄
     */
    @Size(max = 33,message = "年龄长度不能超过33")
    private String age;

    /**
     * 症状
     */
    @Size(max = 255,message = "症状长度不能超过255")
    private String symptom;

    /**
     *
     * @see com.linkwechat.common.enums.SexEnums
     *
     * 患者性别代码, 0 = 未知, 1 = 男, 2 = 女
     */
    private Integer sex;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date appointmentTime;

    /**
     * 留电时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date leavePhoneTime;

    /**
     * 链接
     */
    @Size(max = 1000,message = "链接长度不能超过1000")
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
     * 线索状态(0待分配，1跟进中，2以上门，3已退回，4线索转移)
     */
    @NotNull(message = "线索状态必填")
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
     * 回收次数
     */
    private Integer recoveryTimes;

    /**
     * 末次回收原因
     */
    private String returnReason;

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
    @Size(max = 255,message = "客户微信号长度不能超过255")
    private String weixinId;

    /**
     * 咨询项目
     */
    private String consult;

    /**
     * 所属公海
     */
    @NotNull(message = "所属公海必填")
    private Long seaId;

    /**
     * 自定义属性
     */
    private List<WeClientLeadsVo.OutProperties> propertiesList;

}
