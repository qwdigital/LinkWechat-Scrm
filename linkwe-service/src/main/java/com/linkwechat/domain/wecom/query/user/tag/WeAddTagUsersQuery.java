package com.linkwechat.domain.wecom.query.user.tag;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 成员标签入参
 * @date 2021/12/2 18:27
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeAddTagUsersQuery extends WeBaseQuery {

    /**
     * 标签id，非负整型，指定此参数时新增的标签会生成对应的标签id
     */
    private String tagid;

    /**
     * 企业成员ID列表
     */
    private List<String> userlist;

    /**
     * 企业部门ID列表
     */
    private List<Long> partylist;
}
