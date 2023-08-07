package com.linkwechat.domain.wecom.query.user;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 获取成员入参
 * @date 2021/12/2 18:27
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeUserListQuery extends WeBaseQuery {

    /**
     * 获取的部门id
     */
    private Long department_id;

    /**
     * 是否递归获取子部门下面的成员：1-递归获取，0-只获取本部门
     */
    private Integer fetch_child;
}
