package com.linkwechat.domain.moments.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("we_moments_interacte")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeMomentsInteracte extends BaseEntity {
    @TableId
    private Long id;
    //互动人员名称id
    private String interacteUserId;
    //互动人员类型:0:员工；1:客户
    private Integer interacteUserType;
    //互动类型:0:评论；1:点赞
    private Integer interacteType;
    //互动时间
    private Date interacteTime;
    //朋友圈id
    private String momentId;
    //朋友圈创建人id
    @TableField(exist = false)
    private String momentCreteOrId;

    private Integer delFlag;
}
