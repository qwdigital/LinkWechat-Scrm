package com.linkwechat.domain.community.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 新客自动拉群 入参
 */
@Data
public class WeCommunityNewGroupQuery {


    /**
     * 新客拉群主键
     */
    private String id;


    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 添加客户开始时间
     */
    private String startAddTime;


    /**
     * 添加客户结束时间
     */
    private String endAddTime;



    /**
     * 是否进群，1:是 0:否
     */
    private Integer isJoinGroup;


    /**
     * 入群开始时间
     */
    private String startJoinTime;


    /**
     * 入群结束时间
     */
    private String endJoinTime;


    /**
     * 群id
     */
    private String chatId;

    /**
     * 客户渠道标识
     */
    private  String state;


    /**
     * 群渠道标识
     */
    private String groupState;


    /**
     * 客户id
     */
    private String externalUserid;

//    @TableId
//    private Long id;
//
//    /**
//     * 活码名称
//     */
//    @NotNull(message = "活码名不能为空")
//    private String codeName;
//
//
//    /**
//     * 多个员工id，使用逗号隔开。
//     */
//    private String emplList;
//
//
//    /**
//     * 标签id，多个使用逗号隔开
//     */
//    private String tagList;
//
//
//    /**
//     * 是否跳过验证自动加好友
//     */
//    private Boolean skipVerify = true;
//
//
//    /**
//     * 员工活码configId
//     */
//    private String emplCodeConfigId;
//
//
//    /**
//     * 加群引导语
//     */
//    @NotNull(message = "欢迎语不能为空")
//    private String welcomeMsg;
//
//
//    /**
//     * 实际群id，多个实用逗号隔开
//     */
//    private String chatIdList;
//
//
//    /**
//     * 当群满了后，是否自动新建群。0-否；1-是。 默认为0
//     */
//    private Integer autoCreateRoom;
//
//
//    /**
//     * 自动建群的群名前缀，当auto_create_room为1时有效。最长40个utf8字符
//     */
//    private String roomBaseName;
//
//
//    /**
//     * 自动建群的群起始序号，当auto_create_room为1时有效
//     */
//    private Integer roomBaseId;
//
//
//
//    /**
//     * 群活码企微信的configId
//     */
//    private String groupCodeConfigId;
//
//    /**
//     * 群活码渠道标识
//     */
//    private String groupCodeState;
//
//
//    /**
//     * 群活码图片地址
//     */
//    private String groupCodeUrl;
//
//
//
//    /**
//     * 链接标题
//     */
//    private String linkTitle;
//
//    /**
//     * 链接描述
//     */
//    private String linkDesc;
//
//    /**
//     * 链接封面
//     */
//    private String linkCoverUrl;




}
