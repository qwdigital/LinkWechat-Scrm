package com.linkwechat.domain.wecom.query.product;

import cn.hutool.core.util.ObjectUtil;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.domain.groupmsg.query.WeAddMsgTemplateQuery;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建商品图册入参
 *
 * @author danmo
 */
@Data
public class QwAddProductQuery extends WeBaseQuery {

    /**
     * 商品id
     */
    private String product_id;
    /**
     * 商品的名称、特色等;不超过300个字
     */
    private String description;

    /**
     * 商品编码；不超过128个字节；只能输入数字和字母
     */
    private String product_sn;

    /**
     * 商品的价格，单位为分
     */
    private Long price;

    /**
     * 附件
     */
    private List<WeMessageTemplate> messageTemplates;

    /**
     * 附件类型，仅支持image，最多不超过9个附件
     */
    private List<WeAddMsgTemplateQuery.Attachments> attachments;


    public void setAttachments(List<WeMessageTemplate> messageTemplates) {
        this.attachments = new ArrayList<>(16);
        this.messageTemplates.forEach(messageTemplate -> {
            if (ObjectUtil.equal(MessageType.IMAGE.getMessageType(), messageTemplate.getMsgType())) {
                WeAddMsgTemplateQuery.Attachments images = new WeAddMsgTemplateQuery.Images(messageTemplate.getMsgType(), messageTemplate.getMediaId(),
                        messageTemplate.getPicUrl());
                attachments.add(images);
            }
        });
        this.messageTemplates = null;
    }
}
