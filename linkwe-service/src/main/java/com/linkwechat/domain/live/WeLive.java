package com.linkwechat.domain.live;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.converter.LiveStateConverter;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.qr.WeQrAttachments;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 直播主表
 * @TableName we_live
 */
@TableName(value ="we_live")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class WeLive extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    @ExcelIgnore
    private Long id;

    //直播人名称
    @TableField(exist = false)
    @ExcelProperty("直播成员")
    private String liveUserName;

    /**
     * 直播标题
     */
    @ExcelProperty("直播标题")
    private String liveTitle;


    //部门名称
    @TableField(exist = false)
    @ExcelProperty("所属部门")
    private String deptName;



    /**
     * 直播状态(0:预约中;1:直播中;2:已结束;3:已过期;4:已取消)
     */
    @ExcelProperty(value = "直播状态", converter = LiveStateConverter.class)
    private Integer liveState;


    /**
     * 不等于的状态
     */
    @TableField(exist = false)
    @ExcelIgnore
    private List<Integer> neliveStates;


//    /**
//     * 预约开始时间
//     */
//    @DateTimeFormat("yyyy-MM-dd HH:mm")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
//    @ExcelProperty("预约开始时间")
//    private  Date reserveStart;


    /**
     * 实际开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @ExcelIgnore
    private Date actualStartTime;


    /**
     * 实际开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @ExcelIgnore
    private Date actualEndTime;


    //持续时长 n小时k分
    @TableField(exist = false)
    @ExcelProperty("预约持续时间")
    private String livingDurationDesc;


    //持续时长 n小时k分
    @TableField(exist = false)
    @ExcelIgnore
    private String livingActualDurationDesc;




    @ExcelProperty("创建人")
    private String createBy;


    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty("创建时间")
    private Date createTime;








    /**
     * 直播成员
     */
    @ExcelIgnore
    private String liveWeUserid;

    /**
     * 直播开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ExcelIgnore
    private Date liveStartDate;


    /**
     * 直播开始时间
     */
    @JsonFormat(pattern = "HH:mm" , timezone = "GMT+8")
    @ExcelIgnore
    private Date liveStartTime;


    /**
     * 直播结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ExcelIgnore
    private Date liveEndDate;



    /**
     * 直播结束时间
     */
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    @ExcelIgnore
    private Date liveEndTime;

//    /**
//     * 直播预约时长，单位为秒
//     */
//    @ExcelIgnore
//    private Long reserveLivingDuration;



    /**
     * 直播预约持续时长，存秒入1小时存3600秒
     */
    @ExcelIgnore
    private Integer livingDuration;



    /**
     * 直播实际持续时长，存秒入1小时存3600秒
     */
    @ExcelIgnore
    private Integer livingActualDuration;



    /**
     * 直播简介
     */
    @ExcelIgnore
    private String liveDesc;

    /**
     * 企业微信api返回的直播id
     */
    @ExcelIgnore
    private String livingId;

    /**
     * 观看直播总人数
     */
    @ExcelIgnore
    private Integer viewerNum;

    /**
     * 当前在线观看人数
     */
    @ExcelIgnore
    private Integer onlineCount;

    /**
     * 连麦发言人数
     */
    @ExcelIgnore
    private Integer micNum;

    /**
     * 直播评论人数
     */
    @ExcelIgnore
    private Integer commentNum;

    /**
     * 直播预约人数
     */
    @ExcelIgnore
    private Integer subscribeCount;

    /**
     * 开播提醒时间比如，开播五分钟前,一小时前，一天前等，全部转成秒数值存储
     */
    @ExcelIgnore
    private Integer startReminder;

    /**
     * 开播提醒具体时间,根据live_start_time与start_reminder换算的
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ExcelIgnore
    private Date startSpecReminder;


    /**
     * 是否开启回放，1表示开启，0表示关闭
     */
    @ExcelIgnore
    private Integer openReplay;

    /**
     * open_replay为1时才返回该字段。0表示生成成功，1表示生成中，2表示回放已删除，3表示生成失败
     */
    @ExcelIgnore
    private Integer replayStatus;

    /**
     * 发送目标1:客户;2:客群
     */
    @ExcelIgnore
    private Integer targetType;

    /**
     * 头像
     */
    @ExcelIgnore
    @TableField(exist = false)
    private String avatar;


    /**
     * 发送成员,多个使用逗号隔开，为空的时候则为全部成员
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = FastjsonTypeHandler.class)
    @ExcelIgnore
    private SendWeuser sendWeUser;



    /**
     * 发送客户目标条件为空则为全部客户，其他条件则为部分客户
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = FastjsonTypeHandler.class)
    @ExcelIgnore
    private SendTarget sendTarget;


    /**
     * 标签或群主
     */
    @TableField(exist = false)
    @ExcelIgnore
    private String tagNamesOrGroupOwners;








    //直播或分享链接
    @TableField(exist = false)
    @ExcelIgnore
    private String shareOrJoinUrl;



    //发送人员名称,多个使用逗号隔开
    @TableField(exist = false)
    @ExcelIgnore
    private String sendWeuserNames;

    //发送目标名称,多个使用逗号隔开
    @TableField(exist = false)
    @ExcelIgnore
    private String sendTargetNames;



    /**
     * 删除标识 0 正常 1 删除
     */
    @TableLogic
    @ExcelIgnore
    private Integer delFlag;



    //欢迎语素材新增编辑传入
    @TableField(exist = false)
    @ExcelIgnore
    private List<WeMessageTemplate> attachments;

    //欢迎语素材返回展示
    @TableField(exist = false)
    @ExcelIgnore
    private List<WeLiveAttachments> weLiveAttachments;


    //消息推送成员
    @Data
    public static class SendWeuser{

        private List<String> weUserIds;
    }

    //发送目标条件
    @Data
    public static class  SendTarget{

        //客户标签id或者群主id
        private List<String> tagIdsOrOwnerIds;

    }




}