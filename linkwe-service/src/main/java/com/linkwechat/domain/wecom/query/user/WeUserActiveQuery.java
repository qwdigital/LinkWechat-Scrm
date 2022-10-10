package com.linkwechat.domain.wecom.query.user;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 获取企业活跃成员数入参
 * @date 2021/12/2 18:27
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeUserActiveQuery extends WeBaseQuery {


    /**
     * 具体某天的活跃人数，最长支持获取30天前数据
     */
    private String date;
}
