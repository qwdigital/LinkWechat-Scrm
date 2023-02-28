package com.linkwechat.domain.live;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 直播员工群发通知消息表
 * @TableName we_live_tip
 */
@TableName(value ="we_live_tip")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeLiveTip extends BaseEntity{
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 发送人id
     */
    private String sendWeUserid;


    /**
     * 企业群发消息的id
     */
    private String msgId;

    /**
     * 发送目标id
     */
    private String sendTargetId;

    /**
     * 直播主表id
     */
    private Long liveId;





    //多个使用逗号隔开
    @TableField(exist = false)
    private String liveIds;

    /**
     * 发送状态：0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败
     */
    private Integer sendState;

    /**
     * 发送目标1:客户;2:客群
     */
    private Integer sendTargetType;


    //发送人名称
    @TableField(exist = false)
    private String sendWeuserName;

    //发送目标名称
    @TableField(exist = false)
    private String sendTargetName;


    /**
     * 删除标识 0 有效 1删除
     */
    @TableLogic
    private Integer delFlag;


}