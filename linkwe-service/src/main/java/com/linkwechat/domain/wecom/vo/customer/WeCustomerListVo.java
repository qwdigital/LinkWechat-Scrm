package com.linkwechat.domain.wecom.vo.customer;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @Description 获取客户列表入参
 * @date 2021/12/2 16:11
 **/
@Data
public class WeCustomerListVo extends WeResultVo {
    /**
     * 企业成员的userid
     */
    private List<String> externalUserId;
}
