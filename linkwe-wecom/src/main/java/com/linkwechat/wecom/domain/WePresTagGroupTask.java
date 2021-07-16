package com.linkwechat.wecom.domain;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("we_pres_tag_group")
public class WePresTagGroupTask extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     *主键ID
     */
    @TableId
    private Long taskId = SnowFlakeUtil.nextId();

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 加群引导语
     */
    private String welcomeMsg;

    /**
     * 发送方式 0: 企业群发 1：个人群发
     */
    private Integer sendType;

    /**
     * 群活码id
     */
    private Long groupCodeId;

    /**
     * 发送范围 0: 全部客户 1：部分客户
     */
    private Integer sendScope;

    /**
     * 发送性别 0: 全部 1： 男 2： 女 3：未知
     */
    private Integer sendGender;

    /**
     * 目标客户被添加起始时间
     */
    private String cusBeginTime;

    /**
     * 目标客户被添加结束时间
     */
    private String cusEndTime;

    /**
     * 企业群发消息的id
     */
    private String msgid;

    /**
     * 逻辑删除字段
     */
    @TableLogic(value = "0", delval = "1")
    private Integer delFlag;
}
