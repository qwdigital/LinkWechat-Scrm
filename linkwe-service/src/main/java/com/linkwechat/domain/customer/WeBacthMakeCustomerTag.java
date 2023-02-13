package com.linkwechat.domain.customer;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 客户批量编辑标签实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeBacthMakeCustomerTag {


    private List<WeMakeCustomerTag> weMakeCustomerTagList;


    //true 批量打标签 false  批量移除标签
    private boolean addOrRemove;


}
