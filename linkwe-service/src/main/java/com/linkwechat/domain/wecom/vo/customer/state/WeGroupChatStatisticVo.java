package com.linkwechat.domain.wecom.vo.customer.state;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author danmo
 */
@Data
public class WeGroupChatStatisticVo extends WeResultVo {


    private List<GroupchatStatisticData> items;

    @Data
    public static class GroupchatStatisticData {

        /**
         * 数据日期，为当日0点的时间戳
         */
        @JSONField(format="unixtime")
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
        private Date statTime;

        private String owner;


        private StatisticData data;


    }

    @Data
    public static class StatisticData {
        /**
         * 新增客户群数量
         */
        private Integer newChatCnt;
        /**
         * 截至当天客户群总数量
         */
        private Integer chatTotal;
        /**
         * 截至当天有发过消息的客户群数量
         */
        private Integer chatHasMsg;
        /**
         * 客户群新增群人数
         */
        private Integer newMemberCnt;
        /**
         * 截至当天客户群总人数
         */
        private Integer memberTotal;
        /**
         * 截至当天有发过消息的群成员数
         */
        private Integer memberHasMsg;
        /**
         * 截至当天客户群消息总数
         */
        private Integer msgTotal;
    }
}
