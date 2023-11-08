package com.linkwechat.domain.community.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.community.WeEmpleCodeTag;
import com.linkwechat.domain.community.WeEmpleCodeUseScop;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WeCommunityNewGroupVo extends BaseEntity {

    private Long id;

    /**
     * 活码名称
     */
    private String codeName;

//    @JsonIgnore
//    private Long groupCodeId;

    @JsonIgnore
    private Long emplCodeId;

    /**
     * 员工活码URL
     */
    private String emplCodeUrl;

    /**
     * 欢迎语(即员工活码设定的欢迎语)
     */
    @TableField(exist = false)
    private String welcomeMsg;

//    /**
//     * 群活码信息
//     */
//    @TableField(exist = false)
//    private WeGroupCodeVo groupCodeInfo;

    /**
     * 群活码url
     */
    private String codeUrl;
    /**
     * 员工信息
     */
    @TableField(exist = false)
    private List<WeEmpleCodeUseScop> emplList;

    /**
     * 对应群聊信息
     */
    @TableField(exist = false)
    private List<WeGroup> groupList;

    /**
     * 客户标签列表
     */
    @TableField(exist = false)
    private List<WeEmpleCodeTag> tagList;

    /**
     * 当前添加的好友数
     */
    @TableField(exist = false)
    private Integer cusNumber = 0;

    /**
     * 是否跳过验证
     */
    @TableField(exist = false)
    private Boolean skipVerify = true;

    /**
     * 创建者
     */
    private String createBy;


    /**
     * 实际群名
     */
    private String actualGroupName;

    /**
     * 链接标题
     */
    private String linkTitle;

    /**
     * 链接描述
     */
    private String linkDesc;

    /**
     * 链接封面
     */
    private String linkCoverUrl;


    /**
     * 实际群id，多个实用逗号隔开
     */
    private String chatIdList;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime = new Date();

}
