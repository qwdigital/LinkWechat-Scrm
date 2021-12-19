package com.linkwechat.wecom.domain.vo;

import com.linkwechat.wecom.domain.WeTag;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 客户打标签实体
 * @author: HaoN
 * @create: 2020-10-24 20:09
 **/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeMakeCustomerTag {
    private String externalUserid;
    private String userId;
    private List<WeTag> addTag;
    //是否是企业标签true是;false:不是
    private Boolean isCompanyTag=true;

}
