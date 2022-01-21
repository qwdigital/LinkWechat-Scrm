package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 标签组对象 we_tag_group
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_tag_group")
public class WeTagGroup extends BaseEntity
{
    private static final long serialVersionUID = 1L;



    /** 分组名称 */
    private String gourpName;

    /** 标签 */
    @TableField(exist = false)
    private List<WeTag> weTags;

    /**新增标签 */
    @TableField(exist = false)
    private List<WeTag> addWeTags;


    /** 分组id */
    @TableId
    private String groupId;


    @TableLogic
    private Integer delFlag;


    /**标签分组类型(1:企业客户标签;2:群标签；3:个人标签)*/
    private Integer groupTagType;






}
