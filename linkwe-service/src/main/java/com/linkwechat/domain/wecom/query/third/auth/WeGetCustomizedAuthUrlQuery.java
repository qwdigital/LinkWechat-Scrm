package com.linkwechat.domain.wecom.query.third.auth;

import cn.hutool.core.collection.ListUtil;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.*;

import java.util.List;

/**
 * @author danmo
 * @description 获取带参授权链接
 * @date 2022/3/13 10:56
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeGetCustomizedAuthUrlQuery extends WeBaseQuery {
    /**
     * state值
     */
    private String state;

    /**
     * state值
     */
    private List<String> templateid_list;

    public WeGetCustomizedAuthUrlQuery() {
    }

    public WeGetCustomizedAuthUrlQuery(String state,String templateId){
        this.state=state;
        this.templateid_list= ListUtil.toList(templateId);
    }

    public WeGetCustomizedAuthUrlQuery(String corpId, String state, List<String> templateid_list) {
        setCorpid(corpId);
        this.state = state;
        this.templateid_list = templateid_list;
    }
}
