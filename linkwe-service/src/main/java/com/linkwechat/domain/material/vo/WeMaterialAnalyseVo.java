package com.linkwechat.domain.material.vo;

import com.linkwechat.domain.material.entity.WeMaterial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeMaterialAnalyseVo extends WeMaterial {
    private int viewByTotalNum = 0;
    private int viewTotalNum = 0;
    private int sendTotalNum = 0;
    private Integer materialNum;
    private Long talkId;
    private String talkTitle;
}
