package com.linkwechat.domain.wecom.vo.customer.moment;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;

/**
 * @author danmo
 * @Description 创建客户朋友圈的发表任务
 * @date 2021/12/2 16:11
 **/
@Data
public class WeAddMomentVo extends WeResultVo {
    /**
     * 异步任务id，最大长度为64字节，24小时有效
     */
    private String jobId;
}
