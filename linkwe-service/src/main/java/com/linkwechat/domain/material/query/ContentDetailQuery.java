package com.linkwechat.domain.material.query;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentDetailQuery {

    /**
     * 素材Id
     */
    private Long contentId;

    /**
     * 话术中心Id
     */
    private Long talkId;

    /**
     * 1素材模块 2话术模块
     */
    private Integer moduleType;

    /**
     * 1 发送明细 2查看明细 3话术明细
     */
    private Integer detailsType = 1;

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 话术类型 0企业话术 1客服话术
     */
    private Integer talkType = 0;

    /**
     * 来源类型（1素材，2企业话术，3客服话术）
     */
    private Integer resourceType = 1;

    List<Long> moduleTypeSonList = new ArrayList<>(Arrays.asList(1L));
}
