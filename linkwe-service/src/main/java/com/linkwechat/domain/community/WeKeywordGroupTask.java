package com.linkwechat.domain.community;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.community.vo.WeGroupCodeVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 社区运营 - 关键词拉群任务实体
 */
@Data
@TableName("we_keyword_group")
public class WeKeywordGroupTask extends BaseEntity{

    /**
     * 主键id
     */
    @TableId("id")
    private Long taskId;

    /**
     * 任务名称
     */
    @Size(max = 100, message = "任务名最大长度100")
    @NotNull(message = "任务名不能为空")
    private String taskName;

    /**
     * 群活码id
     */
    @NotNull(message = "群活码不能为空")
    private Long groupCodeId;

    /**
     * 加群引导语
     */
    @Size(max = 255, message = "引导语过长,最大长度255")
    @NotNull(message = "引导语不可为空")
    private String welcomeMsg;

    /**
     * 关键词
     */
    @Size(max = 255, message = "关键词长度过长。最大长度255")
    @NotNull(message = "关键词不可为空")
    private String keywords;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer delFlag;


    /**
     * 实际群聊名称
     */
    @TableField(exist = false)
    private String groupNameList;

    /**
     * 群活码信息
     */
    @TableField(exist = false)
    private WeGroupCodeVo groupCodeInfo;


    /**
     * 任务名称或关键词
     */
    @TableField(exist = false)
    private String taskNameOrKeys;
 }
