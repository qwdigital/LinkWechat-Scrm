package com.linkwechat.domain.wecom.entity.customer.moment;

import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @Description 指定的发表范围
 * @date 2021/12/6 13:48
 **/
@Data
public class WeMomentCustomersEntity {

    /**
     * 可见到该朋友圈的客户标签列
     */
    private List<String> tag_list;
}
