package com.linkwechat.domain.wecom.vo.customer.moment;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 获取客户朋友圈企业发表的列表
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeMomentTaskVo extends WeResultVo {
    /**
     * 朋友圈列表
     */
    private List<WeMomentTask> taskList;

    @Data
    public static class WeMomentTask {
        /**
         * 发表成员用户userid
         */
        private String userId;
        /**
         * 成员发表状态。0:未发表 1：已发表
         */
        private Integer publishStatus;
    }
}
