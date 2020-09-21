package com.linkwechat.wecom.domain.vo;

import com.linkwechat.wecom.domain.WeTag;
import lombok.Data;

import java.util.List;

@Data
public class WeCustomerMakeLabelVo {
    private String userid;

    private String externalUserid;

    private List<WeTag> addTag;

    private List<WeTag> removeTag;

}
