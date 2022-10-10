package com.linkwechat.domain.wecom.query.user;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 邀请成员入参
 * @date 2021/12/2 18:27
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeUserInviteQuery extends WeBaseQuery {

    /**
     * 成员ID列表, 最多支持1000个。
     */
    private List<String> user;

    /**
     * 部门ID列表，最多支持100个。
     */
    private List<Integer> party;

    /**
     * 标签ID列表，最多支持100个。
     */
    private List<String> tag;
}
