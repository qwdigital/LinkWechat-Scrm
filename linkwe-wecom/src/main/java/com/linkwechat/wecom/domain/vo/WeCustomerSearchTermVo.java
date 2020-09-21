package com.linkwechat.wecom.domain.vo;

import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/21 0021 23:45
 */
@Data
public class WeCustomerSearchTermVo extends BaseEntity {
    private String name;

    private String userId;

    private String tagIds;
}
