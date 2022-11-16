package com.linkwechat.domain.material.query;

import lombok.Data;

/**
 * 话术中心
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/10/27 16:49
 */
@Data
public class WeTalkQuery {

    /**
     * 分组Id
     */
    private Long categoryId;

    /**
     * 话术标题
     */
    private String talkTitle;

    /**
     * 话术类型（0企业话术1客服话术）
     */
    private Integer talkType;

}
