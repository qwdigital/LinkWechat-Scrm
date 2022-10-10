package com.linkwechat.domain.wecom.vo.user;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 成员列表
 * @date 2021/12/7 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeUserConvertVo extends WeResultVo {
    /**
     * 该openid在企业微信对应的成员userid
     */
    private String userId;

    /**
     * 企业微信成员userid对应的openid
     */
    private String openId;
}
