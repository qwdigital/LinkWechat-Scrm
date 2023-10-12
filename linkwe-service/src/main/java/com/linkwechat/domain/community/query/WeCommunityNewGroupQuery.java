package com.linkwechat.domain.community.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 新客自动拉群 入参
 */
@Data
public class WeCommunityNewGroupQuery {

    @TableId
    private Long id;
    /**
     * 活码名称
     */
    @NotNull(message = "活码名不能为空")
    private String codeName;

    /**
     * 指定的员工(id)
     */
    @NotNull(message = "使用员工不能为空")
    private List<String> emplList;

    /**
     * 欢迎语
     */
    @NotNull(message = "欢迎语不能为空")
    private String welcomeMsg;

    /**
     * 群活码ID
     */
    @NotNull(message = "群活码不能为空")
    private Long groupCodeId;




    /**
     * 标签id列表
     */
    private List<String> tagList;

    /**
     * 是否跳过验证自动加好友
     */
    private Boolean skipVerify = true;


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
     * 客群名称，多个使用逗号隔开
     */
    @TableField(exist = false)
    private String groupNames;

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
}
