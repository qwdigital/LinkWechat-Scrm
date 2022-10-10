package com.linkwechat.domain.wecom.vo.agentdev;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author leejoker
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WeTransformCorpVO extends WeResultVo {
    /**
     * 仅限第三方服务商，转换已获授权企业的corpid
     */
    private String openCorpId;
}
