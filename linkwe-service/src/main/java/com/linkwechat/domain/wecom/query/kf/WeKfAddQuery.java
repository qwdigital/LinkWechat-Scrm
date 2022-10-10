package com.linkwechat.domain.wecom.query.kf;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @description 客服接口入参
 * @date 2021/12/13 10:27
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeKfAddQuery extends WeBaseQuery {

    /**
     * 客服名称
     */
    private String name;

    /**
     * 客服头像临时素材
     */
    private String media_id;
}
