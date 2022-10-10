package com.linkwechat.web.domain.vo;

import lombok.Data;

/**
 * @author leejoker
 * @version 1.0
 * @date 2022/4/19 13:23
 */
@Data
public class RoleUserVo {
    private Long userId;
    private String userName;
    private String nickName;
    private String weUserId;
    private String openUserid;
}
