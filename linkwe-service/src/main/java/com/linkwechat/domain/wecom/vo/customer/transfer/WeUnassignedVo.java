package com.linkwechat.domain.wecom.vo.customer.transfer;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @Description 待分配的离职成员列表
 * @date 2021/12/2 16:11
 **/
@Data
public class WeUnassignedVo extends WeResultVo {
    /**
     * 客户
     */
    private List<UnassignedVo> info;


    @Data
    public static class UnassignedVo{
        /**
         * 离职成员的userid
         */
        private String handoverUserId;

        /**
         * 外部联系人userid
         */
        private String externalUserId;

        /**
         * 成员离职时间
         */
        private Long dimissionTime;
    }

    /**
     * 是否是最后一条记录
     */
    private Boolean isLast;
}
