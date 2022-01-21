package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 企业微信标签对象 we_tag
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_tag")
public class WeTag implements Serializable
{

    /** 标签组id */
     private String groupId;

     /** 标签名 */
     private String name;


     /** 微信端返回的id */
     @TableId
     private String tagId;


     @TableLogic
     private Integer delFlag;


     private Integer tagType;

     /**所属人*/
     private String owner;





}
