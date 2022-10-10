package com.linkwechat.domain.wecom.query.agentdev;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;

import java.util.List;

/**
 * @author leejoker
 */
@Data
public class WeTransformUserIdQuery extends WeBaseQuery {
    /**
     * 获取到的成员ID List
     */
    private List<String> userid_list;
}
