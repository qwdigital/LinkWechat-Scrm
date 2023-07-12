package com.linkwechat.domain.material.query;

import lombok.*;

import java.util.Date;

/**
 * 素材中心-移动端查看素材请求
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/06 10:30
 */

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

public class WeMaterialMobileAddViewRequest {


    private String openid;
    private String unionid;
    private String nickName;
    private String avatar;

    private String sendUserId;

    private String talkId;
    private String contentId;
    private String resourceType;

    /**
     * 查看开始时间
     */
    private Date viewStartTime;

    /**
     * 数据类型 0心跳 1数据
     */
    private Integer type;

}
