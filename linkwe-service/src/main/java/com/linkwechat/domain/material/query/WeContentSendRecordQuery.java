package com.linkwechat.domain.material.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeContentSendRecordQuery {

    /**
     * 该值企业话术时传递
     * <p>
     * 当resourceType为2是 contentId为企业话术Id
     * 当resourceType为3是 contentId为客服话术Id
     */
    private Long talkId;

    /**
     * 素材Id
     */
    private Long contentId;

    /**
     * 发送者
     */
    private String sendBy;

    /**
     * 发送者Id
     */
    private Long sendById;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 素材类型(1素材中心，2企业话术,3客服话术)
     */
    private Integer resourceType;
}
