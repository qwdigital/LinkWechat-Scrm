package com.linkwechat.domain.envelopes.query;

import lombok.Data;

@Data
public class H5RedEnvelopesParmQuery {
    //企业红包id
    private String redenvelopesId;
    //红包金额
    private int redEnvelopeAmount;
    //红包名
    private String redEnvelopeName;
    //发送人id
    private String sendUserId;
    //客户id
    private String externalUserid;
    //红包数量
    private Integer redEnvelopeNum;
    //群id
    private String chatId;
    //红包类型 1:普通红包 2:拼手气红包
    private Integer redEnvelopesType;
}
