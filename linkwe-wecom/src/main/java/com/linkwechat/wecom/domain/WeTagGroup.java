package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_tag_group")
public class WeTagGroup
{
    private static final long serialVersionUID = 1L;



    /** 分组名称 */
    private String gourpName;

    /** 标签 */
    @TableField(exist = false)
    private List<WeTag> weTags;

    /** 帐号状态（0正常 1删除） */
    private String status=new String("0");

    /** 分组id */
    @TableId
    private String groupId;


    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime=new Date();





}
