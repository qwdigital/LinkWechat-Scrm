package com.linkwechat.domain.community;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 新客自动拉群
 */
@Data
@TableName("we_community_new_group")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeCommunityNewGroup extends BaseEntity {

    /**
     *主键ID
     */
    @TableId
    private Long id;

    /**
     * 员工活码名称
     */
    private String emplCodeName;

//    /**
//     * 群活码id
//     */
//    private Long groupCodeId;

    /**
     * 员工活码id
     */
    private Long emplCodeId;


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
     * 群活码企微信的configId
     */
    private String configId;

    /**
     * 渠道标识
     */
    private String state;


    /**
     * 实际群id，多个实用逗号隔开
     */
    private String chatIdList;


    /**
     * 当群满了后，是否自动新建群。0-否；1-是。 默认为0
     */
    private Integer autoCreateRoom;


    /**
     * 自动建群的群名前缀，当auto_create_room为1时有效。最长40个utf8字符
     */
    private String roomBaseName;


    /**
     * 自动建群的群起始序号，当auto_create_room为1时有效
     */
    private Integer roomBaseId;


    /**
     * 群码地址
     */
    private String codeUrl;

    /**
     * 0 未删除 1 已删除
     */
    @TableLogic
    private int delFlag;
}
