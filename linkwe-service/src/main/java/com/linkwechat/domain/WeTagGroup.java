package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@TableName("we_tag_group")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeTagGroup extends BaseEntity {

    /**
     * 主键
     */
    @TableId
    private Long id;


    /**
     * 企业微信返回的id
     */
    private String groupId;

    /**
     * 标签组名
     */
    private String groupName;

//    @TableField(exist = false)
//    private String gourpName;
    /**
     * 标签分组类型(1:客户企业标签;2:群标签;3:客户个人标签)
     */
    private Integer groupTagType;

    /**
     * 标签所属人
     */
    private String owner;


    /** 标签 */
    @TableField(exist = false)
    private List<WeTag> weTags;


    private Integer delFlag;

}
