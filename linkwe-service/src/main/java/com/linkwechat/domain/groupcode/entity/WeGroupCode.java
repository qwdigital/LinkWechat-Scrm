package com.linkwechat.domain.groupcode.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 客户群活码对象 we_group_code
 *
 * @author ruoyi
 * @date 2020-10-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("we_group_code")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeGroupCode extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId
    private Long id;

    /**
     * 活码URL
     */
    private String codeUrl;



    /**
     * 活码名称
     */
    @Size(max = 60, message = "活码名称最大长度为60个字符")
    @NotBlank(message = "活码名称不能为空")
    private String activityName;


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
     * 实际群id，多个实用逗号隔开
     */
    private String chatIdList;


    /**
     * 配置id
     */
    private String configId;


    /**
     *  企业自定义的state参数，用于区分不同的入群渠道。
     */
    private String state;


    /**
     * 0:正常; 1:删除;
     */
    @TableLogic(value = "0", delval = "1")
    @TableField("del_flag")
    private Integer delFlag ;


    /**
     * 活码客群数
     */
    @TableField(exist = false)
    private int chatGroupNum;


    /**
     * 群总人数
     */
    @TableField(exist = false)
    private int chatGroupMemberTotalNum;


    /**
     * 进群人数
     */
    @TableField(exist = false)
    private int joinChatGroupTotalMemberNum;


    /**
     * 退群人数
     */
    @TableField(exist = false)
    private int exitChatGroupTotalMemberNum;


    /**
     * 昨日以前群总人数
     */
    @TableField(exist = false)
    private int oldChatGroupMemberTotalNum;


    /**
     * 今日以前进群人数
     */
    @TableField(exist = false)
    private int newJoinChatGroupTotalMemberNum;


    /**
     * 昨日以前进群人数
     */
    @TableField(exist = false)
    private int oldJoinChatGroupTotalMemberNum;


    /**
     * 昨日以前退群人数
     */
    @TableField(exist = false)
    private int oldExitChatGroupTotalMemberNum;

    /**
     * 标签名,多个逗号隔开
     */
    @TableField(exist = false)
    private String tags;


    /**
     * 标签id,多个逗号隔开
     */
    @TableField(exist = false)
    private String tagIds;


    /**
     * 客群名称，多个使用逗号隔开
     */
    @TableField(exist = false)
    private String groupNames;


    @ApiModelProperty("活码短链")
    @TableField(exist = false)
    private String qrShortLink;

}
