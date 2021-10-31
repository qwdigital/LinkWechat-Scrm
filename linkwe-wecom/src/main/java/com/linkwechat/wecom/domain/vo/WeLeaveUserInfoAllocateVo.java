package com.linkwechat.wecom.domain.vo;

import lombok.Data;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/21 0021 23:45
 */
@Data
public class WeLeaveUserInfoAllocateVo {
    //移交人
    private String handoverUserid;
    //接管人
    private String takeoverUserid;
}
