package com.linkwechat.domain.moments.query;

import com.linkwechat.common.core.domain.entity.SysUser;
import lombok.Data;

/**
 * 同步成员群发MQ参数
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/04 15:27
 */
@Data
public class WeMomentsSyncGroupSendMqRequest extends WeMomentsSyncGroupSendRequest {

    /**
     * 执行成员
     */
    private SysUser user;

    /**
     * 执行次数
     */
    private Integer num;


}
