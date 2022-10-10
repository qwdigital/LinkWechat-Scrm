package com.linkwechat.domain.wecom.query.customer.transfer;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 待分配的离职成员列表入参
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeUnassignedQuery extends WeBaseQuery {
    /**
     * 分页查询，要查询页号，从0开始
     */
    private Integer page_id;

    /**
     * 每次返回的最大记录数，默认为1000，最大值为1000
     */
    private Integer page_size;


    private String cursor;
}
