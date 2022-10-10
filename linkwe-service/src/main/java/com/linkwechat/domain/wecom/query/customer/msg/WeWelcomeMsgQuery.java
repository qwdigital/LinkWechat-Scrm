package com.linkwechat.domain.wecom.query.customer.msg;

import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.wecom.query.WeMsgTemplateQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 发送新客户欢迎语
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeWelcomeMsgQuery extends WeMsgTemplateQuery {

    /**
     * 通过添加外部联系人事件推送给企业的发送欢迎语的凭证，有效期为20秒
     */
    private String welcome_code;

    private List<WeMessageTemplate> messageTemplates;
}
