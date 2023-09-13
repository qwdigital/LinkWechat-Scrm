package com.linkwechat.domain.wecom.vo.customer.link;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;

import java.util.List;


/**
 * 获取获客链接列表
 */
@Data
public class WeLinkCustomerListsVo  extends WeResultVo {

    /**
     * link_id列表
     */
    private List<String> link_id_list;

}
