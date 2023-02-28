package com.linkwechat.domain.wecom.vo.agentdev;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * unionid转换为第三方external_userid
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/10/26 16:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WeUnionidTransformExternalUserIdVO extends WeResultVo {

    /**
     * 该授权企业的外部联系人ID
     */
    private String externalUserid;

    /**
     * 该微信帐号尚未成为企业客户时，返回的临时外部联系人ID，该ID有效期为90天，
     * 当该用户在90天内成为企业客户时，可以通过external_userid查询pending_id关联
     */
    private String pendingId;


}
