package com.linkwechat.domain.wecom.query.customer;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author danmo
 * @Description 获取客户列表入参
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeBatchCustomerQuery extends WeBaseQuery {
    /**
     * 外部联系人的userid
     */
    private List<String> userid_list;

    /**
     * 上次请求返回的next_cursor
     */
    private String cursor;

    /**
     * 限制条数
     */
    private Integer limit;

    public WeBatchCustomerQuery(){

    }


    public WeBatchCustomerQuery(List<String> useridList,String cursor,Integer limit){
        this.userid_list=useridList;
        this.cursor=cursor;
        this.limit=limit;
    }

}
