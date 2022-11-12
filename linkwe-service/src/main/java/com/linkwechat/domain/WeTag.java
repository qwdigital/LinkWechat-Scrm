package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("we_tag")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeTag extends BaseEntity {


    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 微信端返回的id
     */
    private String tagId;

    /**
     * 标签组的主键id
     */
    private String groupId;

    /**
     * 标签名
     */
    private String name;


    /**
     * 1:客户企业标签;2:群标签;3:客户个人标签
     */
    private Integer tagType;


    /**
     * 标签所属人
     */
    private String owner;


    /**
     * 0:正常;1:删除;
     */
//    @TableLogic
    private Integer delFlag;


}
