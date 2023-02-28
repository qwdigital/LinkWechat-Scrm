package com.linkwechat.domain.material.vo.talk;

import com.linkwechat.domain.material.entity.WeContentTalk;
import lombok.Data;

@Data
public class WeContentTalkVo extends WeContentTalk {
    private Integer materialNum = 0;
    private Integer sendTotalNum = 0;
    private Integer viewTotalNum = 0;
    private Integer viewByTotalNum = 0;
    private String  materialIds;
}
