package com.linkwechat.wecom.domain.dto.customer;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.Data;

import java.util.List;

/**
 * @description: 客户列表
 * @author: HaoN
 * @create: 2020-10-19 22:14
 **/
@Data
public class ExternalUserList extends WeResultDto {
    private String[] external_userid;
    private List<ExternalUserDetail> external_contact_list;
    private String next_cursor;
}
