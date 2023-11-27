package com.linkwechat.domain.fission;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.fission.vo.WeExecuteUserOrGroupConditVo;
import com.linkwechat.domain.fission.vo.WeGroupMessageExecuteUserOrGroupTipVo;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.groupmsg.vo.WeGroupMessageExecuteUsertipVo;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.sop.vo.WeSopExecuteUserConditVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 裂变（任务宝,群裂变）
 * @TableName we_fission
 */
@Data
@TableName(value ="we_fission",autoResultMap = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeFission extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 裂变任务名称
     */
    private String fassionName;

    /**
     * 裂变状态（1:待开始;2:进行中;3:已结束; 4:暂停）
     */
    private Integer fassionState;

    /**
     *  裂变类型1:任务宝;2:群裂变
     */
    private Integer fassionType;

    /**
     * 任务开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date fassionStartTime;

    /**
     * 任务结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date fassionEndTime;

    /**
     * 执行老客条件,如果为空,则为全部成员(群发任务创建的群主或客户)
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = FastjsonTypeHandler.class)
    private WeGroupMessageExecuteUsertipVo executeUserOrGroup;

    /**
     * 海报url
     */
    private String posterUrl;


    /**
     * 裂变的url
     */
    private String fissionUrl;

    /**
     * 海报id
     */
    private Long posterId;

    /**
     * 添加员工或群活码
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = FastjsonTypeHandler.class)
    private WeExecuteUserOrGroupConditVo addWeUserOrGroupCode;

    /**
     * 任务文案
     */
    private String content;

    /**
     * 兑奖条件(N人)
     */
    private Integer exchangeTip;

    /**
     * 兑奖方式(1:跳转链接兑奖;2:客服兑奖;3:自助兑奖)
     */
    private Integer exchangeType;

    /**
     * 兑奖内容
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = FastjsonTypeHandler.class)
    private ExchangeContent exchangeContent;


    @TableField(exist = false)
    private WeMaterial wematerial;


    /**
     * 是否发送过消息通知 1:发送过 2:未发送过 3:推送异常
     */
    private Integer isTip;


    @TableLogic
    private Integer delFlag;

    /**
     * 邀请老客数量
     */
    @TableField(exist = false)
    private Integer inviterOldCustomerNum;

    /**
     * 完成任务老客数
     */
    @TableField(exist = false)
    private Integer completeTaskOldCustomerNum;

    /**
     * 裂变新客数
     */
    @TableField(exist = false)
    private Integer fissionCustomerNum;


    /**
     * 活动名称
     */
    private String activeTitle;


    /**
     * 活动描述
     */
    private String activeDescr;

    /**
     * 1:海报缩略图 2:自定义
     */
    private Integer activeCoverType;


    /**
     * 活动封面url
     */
    private String activeCoverUrl;


    /**
     * 客户查询条件
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = FastjsonTypeHandler.class)
    private WeCustomersQuery weCustomersQuery;

    /**
     * 发送人()
     */
    @TableField(exist = false)
    private List<WeAddGroupMessageQuery.SenderInfo> senderList;



    /**
     * 发送范围: 0全部客户 1按条件筛选
     */
    private Integer scopeType;



    @Data
    public static class ExchangeContent{

        /**
         * 兑奖链接
         */
        private  String redemptionLink;


        /**
         * 兑奖规则
         */
        private  String redemptionRule;


        /**
         * 兑奖客服员工id
         */
        private String weUserId;


        /**
         * 兑奖客服员工名
         */
        private String userName;


        /**
         * 兑奖客服员工活码(非微信客服，当前指代员工活码)
         */
        private String customerServiceCodeUrl;

    }



}