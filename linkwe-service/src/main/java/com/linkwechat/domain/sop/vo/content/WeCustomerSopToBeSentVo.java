package com.linkwechat.domain.sop.vo.content;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 待发送客户相关
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeCustomerSopToBeSentVo {
    //客户名称
    private String customerName;

    //客户企业名称
    private String corpName;
    //客户类型
    private Integer customerType;
    //客户id
    private String externalUserid;
    //客户性别
    private Integer gender;
    //客户头像
    private String avatar;
    //发送sop的相关信息
    private List<WeSopToBeSentContentInfoVo> weSopToBeSentContentInfoVo;
}
