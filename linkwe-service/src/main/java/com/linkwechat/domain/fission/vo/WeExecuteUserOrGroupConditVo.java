package com.linkwechat.domain.fission.vo;

import com.linkwechat.domain.groupcode.entity.WeGroupCode;
import com.linkwechat.domain.sop.vo.WeSopExecuteUserConditVo;
import lombok.Data;

@Data
public class WeExecuteUserOrGroupConditVo {

    //员工筛选条件
    WeSopExecuteUserConditVo addWeUser;

    //构建群活码
    WeGroupCode addGroupCode;

}
