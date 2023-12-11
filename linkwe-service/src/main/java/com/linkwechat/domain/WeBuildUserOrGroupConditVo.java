package com.linkwechat.domain;

import com.linkwechat.domain.groupcode.entity.WeGroupCode;
import com.linkwechat.domain.qr.query.WeQrAddQuery;
import lombok.Data;

/**
 * 构建活码或者员工活码条件参数
 */
@Data
public class WeBuildUserOrGroupConditVo {


    //构建员工活码
    WeQrAddQuery weQrAddQuery;


    //构建群活码
    WeGroupCode addGroupCode;
}
