package com.linkwechat.domain.fission.vo;

import com.linkwechat.domain.fission.WeFission;
import com.linkwechat.domain.fission.WeFissionInviterRecordSub;
import lombok.Data;

import java.util.List;

/**
 * 裂变进度详情
 */
@Data
public class WeFissionProgressVo {


    /**
     * 兑奖条件(N人)
     */
    private Integer exchangeTip;


    /**
     * 邀请记录
     */
    private List<WeFissionInviterRecordSub> weFissionInviterRecordSubList;

    /**
     * 任务奖励，为空，则任务未完成
     */
    private WeFission.ExchangeContent exchangeContent;
}
