package com.linkwechat.domain.wecom.vo.customer.moment;

import com.linkwechat.domain.wecom.entity.customer.moment.WeMomentCustomersEntity;
import com.linkwechat.domain.wecom.entity.customer.moment.WeMomentSendsEntity;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;

/**
 * @author danmo
 * @Description 创建客户朋友圈的发表任务
 * @date 2021/12/2 16:11
 **/
@Data
public class WeMomentResultVo extends WeResultVo {
    /**
     * 任务状态，整型，1表示开始创建任务，2表示正在创建任务中，3表示创建任务已完成
     */
    private String status;

    /**
     * 操作类型，字节串，此处固定为add_moment_task
     */
    private String type;

    private MomentResult result;

    @Data
    public static class MomentResult extends WeResultVo{
        /**
         * 朋友圈id
         */
        private String momentId;

        private WeMomentSendsEntity invalidSenderList;

        private WeMomentCustomersEntity invalidExternalContactList;
    }
}
