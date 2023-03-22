package com.linkwechat.domain.fission;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.fission.vo.WeExecuteUserOrGroupConditVo;
import com.linkwechat.domain.fission.vo.WeGroupMessageExecuteUserOrGroupTipVo;
import com.linkwechat.domain.groupmsg.vo.WeGroupMessageExecuteUsertipVo;
import com.linkwechat.domain.sop.vo.WeSopExecuteUserConditVo;
import lombok.Data;

import java.util.Date;

/**
 * 裂变（任务宝,群裂变）
 * @TableName we_fission
 */
@Data
@TableName(value ="we_fission")
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
     * 裂变状态（1:待开始;2:进行中;3:已结束）
     */
    private Integer fassionState;

    /**
     *  裂变类型1:任务宝;2:群裂变
     */
    private Integer fassionType;

    /**
     * 任务开始时间
     */
    private Date fassionStartTime;

    /**
     * 任务结束时间
     */
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
     * 兑奖方式(1:跳转链接兑奖;2:客服兑奖)
     */
    private Integer exchangeType;

    /**
     * 兑奖内容
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = FastjsonTypeHandler.class)
    private ExchangeContent exchangeContent;


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
         * 兑奖客服员工活码(非微信客服，当前指代员工活码)
         */
        private String customerServiceCodeUrl;

    }



}