package com.linkwechat.wecom.domain.vo;

import com.linkwechat.wecom.domain.WeGroupTagRel;
import lombok.Data;

import java.util.List;

@Data
public class WeMakeGroupTagVo {
    private String chatId;
    private List<WeGroupTagRel> weeGroupTagRel;
}
