package com.linkwechat.domain.sop;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.sop.vo.WeSopExecuteConditVo;
import com.linkwechat.domain.sop.vo.WeSopExecuteEndVo;
import com.linkwechat.domain.sop.vo.WeSopExecuteUserConditVo;
import com.linkwechat.domain.strategic.crowd.WeStrategicCrowdSwipe;
import com.linkwechat.typeHandler.WeStrategicCrowdSwipeListTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@TableName(value ="we_sop_base",autoResultMap = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeSopBase extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * sop基础类型(1:客户sop;2:客群sop)
     */
    private Integer baseType;

    /**
     * sop业务类(1:新客sop;2:活动节日sop;3:客户转化sop;4:新群培育sop;5:周期营销sop;6:特定宣发sop)
     */
    private Integer businessType;

    /**
     * 排除的类型
     */
    @TableField(exist = false)
    private Integer neBusinessType;

    /**
     * sop名称
     */
    private String sopName;

    /**
     * sop发送类型(1:企业微信发送;2:手动发送)
     */
    private Integer sendType;

    /**
     * 执行成员条件,如果为空,则为全部成员
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = FastjsonTypeHandler.class)
    private WeSopExecuteUserConditVo executeWeUser;

    /**
     * 被执行的客户或群条件,如果为空,则为全部客户或群(便于前端回显使用)
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = FastjsonTypeHandler.class)
    private WeSopExecuteConditVo executeCustomerOrGroup;

    /**
     * 策略执行条件
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = WeStrategicCrowdSwipeListTypeHandler.class)
    private List<WeStrategicCrowdSwipe>  executeCustomerSwipe;



    /**
     * sop结束内容
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private WeSopExecuteEndVo endContent;


    /**
     * 推送内容设置
     */
    @TableField(exist = false)
    private List<WeSopPushTime> weSopPushTimes;

    /**
     * 群主id
     */
    @TableField(exist = false)
    private String groupLenderId;


    /**
     * sop状态(1:执行中;2:暂停)
     */
    private Integer sopState;


    /**
     * 是否提前结束(是否提前结束:1:提前结束满足任意条件;0:不提前结束;2:提前结束全部任意条件)
     */
    private Integer earlyEnd;

    /**
     * 符合条件执行成员id,多个id逗号隔开
     */
    private String executeWeUserIds;

    /**
     * 删除标识 0 有效 1删除
     */
    @TableLogic
    private Integer delFlag;



    /**
     * 发送范围: 0全部客户 1按条件筛选
     */
    private Integer scopeType;


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

}
