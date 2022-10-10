package com.linkwechat.domain.taggroup.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkwechat.common.enums.MessageNoticeType;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.community.vo.WeCommunityTaskEmplVo;
import com.linkwechat.domain.community.vo.WeGroupCodeVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 老客标签建群任务Vo
 */
@Data
public class WePresTagGroupTaskVo {

    /**
     * 类型。该属性仅用于H5页面与SOP混合列表的任务类型判断
     */
    private final Integer type = MessageNoticeType.TAG.getType();

    /**
     * 老客标签建群任务id
     */
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 加群引导语
     */
    private String welcomeMsg;

    /**
     * 群活吗id
     */
    @JsonIgnore
    private Long codeId;

    /**
     * 群活码连接
     */
    @JsonIgnore
    private String codeUrl;

    private WeGroupCodeVo groupCodeInfo;

    /**
     * 发送方式 0: 企业群发 1：个人群发
     */
    private Integer sendType;

    /**
     * 当前群人数
     */
    private Integer totalMember;

    /**
     * 使用员工
     */
    private List<WeCommunityTaskEmplVo> scopeList;

    /**
     * 标签
     */
    private List<WeTag> tagList;

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
     * msgid
     */
    private String msgid;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String updateTime;

    /**
     * 设置群活码信息
     */
    public void fillGroupCodeVo() {
        WeGroupCodeVo groupCodeVo = new WeGroupCodeVo(this.getCodeId(), this.getCodeUrl());
        this.setGroupCodeInfo(groupCodeVo);
    }
}
