package com.linkwechat.domain.wecom.query.user.tag;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 成员标签入参
 * @date 2021/12/2 18:27
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeAddUserTagQuery extends WeBaseQuery {

    /**
     * 标签名称，长度限制为32个字以内（汉字或英文字母），标签名不可与其他标签重名。
     */
    private String tagname;

    /**
     * 标签id，非负整型，指定此参数时新增的标签会生成对应的标签id
     */
    private String tagid;
}
