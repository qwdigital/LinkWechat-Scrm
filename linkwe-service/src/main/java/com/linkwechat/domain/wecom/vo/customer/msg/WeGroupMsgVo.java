package com.linkwechat.domain.wecom.vo.customer.msg;

import com.linkwechat.domain.wecom.query.WeMsgTemplateQuery;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @Description 消息
 * @date 2021/10/3 16:35
 **/
@Data
public class WeGroupMsgVo {

    /**
     * 企业群发消息的id
     */
    private String msgId;

    /**
     * 群发消息创建者userid，API接口创建的群发消息不返回该字段
     */
    private String creator;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 群发消息创建来源。0：企业 1：个人
     */
    private Integer createType;

    /**
     * 消息文本内容，最多4000个字节
     */
    private WeMsgTemplateQuery.Text text;

    /**
     * 企业群发消息的id
     */
    private List<JSONObject> attachments;
}
