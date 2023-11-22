package com.linkwechat.domain.customer;

import com.linkwechat.domain.WeTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeMakeCustomerTag {
    private String externalUserid;
    private String userId;
    private List<WeTag> addTag;

    private List<WeTag> removeTag;
    //是否是企业标签true是;false:不是
    private Boolean isCompanyTag=true;

    //是否记录
    private boolean isRecord=true;

    //false无需记录轨迹;true:记录轨迹
    private boolean source=true;

    private String extIdAndWeUserId;


}
