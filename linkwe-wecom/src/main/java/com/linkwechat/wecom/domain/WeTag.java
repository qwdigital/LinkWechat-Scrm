package com.linkwechat.wecom.domain;

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

/**
 * 企业微信标签对象 we_tag
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_tag")
public class WeTag
{

    /** 标签组id */
    private String groupId;

    /** 标签名 */
    private String name;

    /** 帐号状态（0正常 2删除） */
    private String status;

     /** 微信端返回的id */
     @TableId
     private String tagId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime=new Date();



}
