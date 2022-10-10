package com.linkwechat.domain.wecom.query.user;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description WeBaseQuery入参
 * @date 2021/12/2 18:27
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeUserQuery extends WeBaseQuery {

    /**
     * 通过成员授权获取到的code
     */
    private String code;
    /**
     * 成员UserID
     */
    private String userid;

    /**
     * 成员UserID列表
     */
    private List<String> useridlist;

    private String user_ticket;
}
