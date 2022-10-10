package com.linkwechat.domain.wecom.query.customer.msg;

import cn.hutool.core.util.ObjectUtil;
import com.linkwechat.domain.wecom.query.WeMsgTemplateQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 创建企业群发入参
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeAddCustomerMsgQuery extends WeMsgTemplateQuery {

    /**
     * 群发任务的类型，默认为single，表示发送给客户，group表示发送给客户群
     */
    private String chat_type;

    /**
     * 客户的外部联系人id列表，仅在chat_type为single时有效，不可与sender同时为空，最多可传入1万个客户
     */
    private List<String> external_userid;

    /**
     * 发送企业群发消息的成员userid，当类型为发送给客户群时必填
     */
    private String sender;

    public void setChat_type(String chat_type) {
        this.chat_type = chat_type;
    }

    public void setChat_type(Integer chatType) {
        if(ObjectUtil.equal(2,chatType)){
            this.chat_type = "group";
        }else {
            this.chat_type = "single";
        }
    }
}
