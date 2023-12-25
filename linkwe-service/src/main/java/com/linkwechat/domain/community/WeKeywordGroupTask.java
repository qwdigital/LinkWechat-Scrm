package com.linkwechat.domain.community;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.WeKeyWordGroupSub;
import com.linkwechat.domain.community.vo.WeGroupCodeVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 社区运营 - 关键词拉群任务实体
 */
@Data
@TableName("we_keyword_group")
public class WeKeywordGroupTask extends BaseEntity{

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String descrition;



    /**
     * 关键词
     */
    private String keywordGroupUrl;


    /**
     * 关键词群链接二维码链接
     */
    private String keywordGroupQrUrl;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer delFlag;


    /**
     * 关键词群
     */
    @TableField(exist = false)
    private List<WeKeyWordGroupSub> keyWordGroupSubs;


    /**
     * 关键词数量
     */
    @TableField(exist = false)
    private Integer keyWordGroupNumber;

    /**
     * 总访问数量
     */
    @TableField(exist = false)
    private Integer totalViewNumber;

    /**
     * 总入群数量
     */
    @TableField(exist = false)
    private Integer totalJoinGroupNmber;


 }
