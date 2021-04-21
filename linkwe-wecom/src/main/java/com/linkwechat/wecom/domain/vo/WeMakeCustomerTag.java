package com.linkwechat.wecom.domain.vo;

import com.linkwechat.wecom.domain.WeTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 客户打标签实体
 * @author: HaoN
 * @create: 2020-10-24 20:09
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeMakeCustomerTag {
    private String externalUserid;
    private List<WeTag> addTag;

}
