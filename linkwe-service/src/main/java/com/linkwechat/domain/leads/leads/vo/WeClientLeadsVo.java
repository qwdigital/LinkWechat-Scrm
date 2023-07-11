package com.linkwechat.domain.leads.leads.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 线索
 *
 * @author WangYX
 * @since 2023-04-04
 */
@Data
public class WeClientLeadsVo implements Serializable {


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
     * 年龄
     */
    private String age;

    /**
     * 症状
     */
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
     * 患者id
     */
    private Long patientId;

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
    private String link;

    /**
     * 一级渠道
     */
    private Long primaryChannel;

    /**
     * 一级渠道名称
     */
    private String primaryChannelName;

    /**
     * 二级渠道
     */
    private Long secondaryChannel;

    /**
     * 二级渠道名称
     */
    private String secondaryChannelName;

    /**
     * 三级渠道
     */
    private Long threeLevelChannel;

    /**
     * 三级渠道名称
     */
    private String threeLevelChannelName;

    /**
     * 主题
     */
    private String subject;

    /**
     * 线索状态(0待分配，1跟进中，2以上门，3已退回)
     */
    private Integer leadsStatus;

    /**
     * 线索状态(0待分配，1跟进中，2以上门，3已退回，4批量线索转移，5个人线索转移)
     * 供前端判断线索状态使用
     */
    private Integer state;


    /**
     * 线索跟进中的子状态。从we_leads_pending_status表中取值
     */
    private String leadsPendingStatus;

    /**
     * 线索跟进中的子状态。从we_leads_pending_status表中取值
     */
    private String leadsPendingStatusName;

    /**
     * 亲属关系
     */
    private String kinship;

    /**
     * 次级关系
     */
    private String secondaryRelation;

    /**
     * 就近院区
     */
    private Long nearbyArea;

    /**
     * 就近院区名称
     */
    private String nearbyAreaName;

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
     * 标签备注名
     */
    private String labelsNames;

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
    private List<OutProperties> propertiesList;

    /**
     * 对外自定义属性值
     */
    @Data
    public static final class OutProperties extends com.linkwechat.domain.leads.leads.vo.WeClientLeadsListVo.Properties {
        private String name;
    }

}
