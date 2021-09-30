package com.linkwechat.wecom.domain.dto.customer;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 客户标签编辑
 * @author: HaoN
 * @create: 2020-10-24 21:01
 **/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CutomerTagEdit {
    /**添加外部联系人的userid*/
    private String userid;
    /**外部联系人userid*/
    private String external_userid;
    /**要标记的标签列表*/
    private String[] add_tag;
    /**要移除的标签列表*/
    private String[] remove_tag;
}
