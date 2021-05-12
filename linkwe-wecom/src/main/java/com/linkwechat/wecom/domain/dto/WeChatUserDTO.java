package com.linkwechat.wecom.domain.dto;

import lombok.Data;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/2/9 14:58
 */
@Data
public class WeChatUserDTO {
    private String userid;
    private String unionid;
    private String name;
    private String avatar;
}
