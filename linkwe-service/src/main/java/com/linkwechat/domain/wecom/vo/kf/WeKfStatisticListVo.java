package com.linkwechat.domain.wecom.vo.kf;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 客服统计
 * @date 2022/10/11 10:27
 **/
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class WeKfStatisticListVo extends WeResultVo {

    /**
     * 统计数据列表
     */
    @ApiModelProperty("统计数据列表")
    private List<KfStatisticVo> statisticList;


    @ApiModel
    @Data
    public class KfStatisticVo{
        /**
         * 数据统计日期
         */
        @ApiModelProperty("数据统计日期")
        private Long statTime;

        /**
         * 一天的统计数据
         */
        @ApiModelProperty("一天的统计数据")
        private KfStatistic statistic;
    }

    @ApiModel
    @Data
    public class KfStatistic{

        /**
         * 咨询会话数。客户发过消息并分配给接待人员或智能助手的客服会话数，转接不会产生新的会话
         */
        @ApiModelProperty("咨询会话数。客户发过消息并分配给接待人员或智能助手的客服会话数，转接不会产生新的会话")
        private Integer sessionCnt;

        /**
         * 咨询客户数。在会话中发送过消息的客户数量，若客户多次咨询只计算一个客户
         */
        @ApiModelProperty("咨询客户数。在会话中发送过消息的客户数量，若客户多次咨询只计算一个客户")
        private Integer customerCnt;

        /**
         * 咨询消息总数。客户在会话中发送的消息的数量
         */
        @ApiModelProperty("咨询消息总数。客户在会话中发送的消息的数量")
        private Integer customerMsgCnt;

        /**
         * 升级服务客户数。通过「升级服务」功能成功添加专员或加入客户群的客户数，若同一个客户添加多个专员或客户群，只计算一个客户
         */
        @ApiModelProperty("升级服务客户数。通过「升级服务」功能成功添加专员或加入客户群的客户数，若同一个客户添加多个专员或客户群，只计算一个客户")
        private Integer upgradeServiceCustomerCnt;

        /**
         * 智能回复会话数。客户发过消息并分配给智能助手的咨询会话数。通过API发消息或者开启智能回复功能会将客户分配给智能助手
         */
        @ApiModelProperty("智能回复会话数。客户发过消息并分配给智能助手的咨询会话数。通过API发消息或者开启智能回复功能会将客户分配给智能助手")
        private Integer aiSessionReplyCnt;

        /**
         * 转人工率。一个自然日内，客户给智能助手发消息的会话中，转人工的会话的占比。
         */
        @ApiModelProperty("转人工率。一个自然日内，客户给智能助手发消息的会话中，转人工的会话的占比。")
        private Float aiTransferRate;

        /**
         * 知识命中率。一个自然日内，客户给智能助手发送的消息中，命中知识库的占比
         */
        @ApiModelProperty("知识命中率。一个自然日内，客户给智能助手发送的消息中，命中知识库的占比")
        private Float aiKnowledgeHitRate;

        /**
         * 人工回复率。一个自然日内，客户给接待人员发消息的会话中，接待人员回复了的会话的占比
         */
        @ApiModelProperty("人工回复率。一个自然日内，客户给接待人员发消息的会话中，接待人员回复了的会话的占比")
        private Float replyRate;

        /**
         * 平均首次响应时长，单位：秒
         */
        @ApiModelProperty("平均首次响应时长，单位：秒")
        private Float firstReplyAverageSec;

        /**
         * 满意度评价发送数。当api托管了会话分配，满意度原生功能失效，满意度评价发送数为0
         */
        @ApiModelProperty("满意度评价发送数。当api托管了会话分配，满意度原生功能失效，满意度评价发送数为0")
        private Integer satisfactionInvestgateCnt;

        /**
         * 满意度参评率 。当api托管了会话分配，满意度原生功能失效
         */
        @ApiModelProperty("满意度参评率 。当api托管了会话分配，满意度原生功能失效")
        private Float satisfactionParticipationRate;

        /**
         * “满意”评价占比 。在客户参评的满意度评价中评价是“满意”的占比
         */
        @ApiModelProperty("“满意”评价占比 。在客户参评的满意度评价中评价是“满意”的占比")
        private Float satisfiedRate;

        /**
         * “一般”评价占比 。在客户参评的满意度评价中，评价是“一般”的占比
         */
        @ApiModelProperty("“一般”评价占比 。在客户参评的满意度评价中，评价是“一般”的占比")
        private Float middlingRate;

        /**
         * “不满意”评价占比。在客户参评的满意度评价中，评价是“不满意”的占比
         */
        @ApiModelProperty("“不满意”评价占比。在客户参评的满意度评价中，评价是“不满意”的占比")
        private Float dissatisfiedRate;

        /**
         * 专员服务邀请数
         */
        @ApiModelProperty("专员服务邀请数")
        private Integer upgradeServiceMemberInviteCnt;

        /**
         * 添加专员的客户数
         */
        @ApiModelProperty("添加专员的客户数")
        private Integer upgradeServiceMemberCustomerCnt;

        /**
         * 	客户群服务邀请数
         */
        @ApiModelProperty("客户群服务邀请数")
        private Integer upgradeServiceGroupChatInviteCnt;

        /**
         * 	加入客户群的客户数
         */
        @ApiModelProperty("加入客户群的客户数")
        private Integer upgradeServiceGroupChatCustomerCnt;
    }
}
