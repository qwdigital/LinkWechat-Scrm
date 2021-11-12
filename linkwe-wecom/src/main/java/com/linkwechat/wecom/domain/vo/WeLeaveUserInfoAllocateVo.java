package com.linkwechat.wecom.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/21 0021 23:45
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeLeaveUserInfoAllocateVo {
    //移交人
    private String handoverUserid;
    //接管人
    private String takeoverUserid;

    //客户id，多个客户使用逗号隔开
    private String externalUserid;

    //0:离职继承;1:在职继承;
    private  Integer extentType;
}
