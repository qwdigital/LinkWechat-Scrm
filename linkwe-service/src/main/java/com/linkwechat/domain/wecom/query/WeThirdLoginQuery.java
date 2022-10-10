package com.linkwechat.domain.wecom.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author danmo
 * @Description 获取凭证
 * @date 2021/12/2 18:27
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeThirdLoginQuery extends WeBaseQuery{

    /**
     * oauth2.0授权企业微信管理员登录产生的code
     */
    private String auth_code;

}
