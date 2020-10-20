package com.linkwechat.wecom.domain.dto.customer;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.Data;

/**
 * @description: 获取配置了客户联系功能的成员列表
 * @author: HaoN
 * @create: 2020-10-19 22:08
 **/
@Data
public class FollowUserList extends WeResultDto {

    private String[] follow_user;
}
