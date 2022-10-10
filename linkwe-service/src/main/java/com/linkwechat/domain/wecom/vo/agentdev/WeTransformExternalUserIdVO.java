package com.linkwechat.domain.wecom.vo.agentdev;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author leejoker
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WeTransformExternalUserIdVO extends WeResultVo {
    /**
     * 该服务商第三方应用下的企业的外部联系人ID
     */
    private String externalUserid;
}
