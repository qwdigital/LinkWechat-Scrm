package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;


/**
 * 关键词群子表
 * @TableName we_key_word_group_sub
 */
@Data
@TableName(value ="we_key_word_group_sub")
public class WeKeyWordGroupSub extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 活码名称
     */
    private String codeName;

    /**
     * 关键词主表主键
     */
    private Long keywordGroupId;

    /**
     * 实际群id，多个使用逗号隔开
     */
    private String chatIdList;


    /**
     *  实际群id，多个使用逗号隔开
     */
    private String groupCodeName;

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
     * 群活码企微信的configId

     */
    private String groupCodeConfigId;

    /**
     * 群活码渠道标识

     */
    private String groupCodeState;

    /**
     * 群活码图片地址

     */
    private String groupCodeUrl;

    /**
     * 排序
     */
    private Integer sort;


    /**
     * 0:正常;1:删除;
     */
    @TableLogic
    private Integer delFlag;


}