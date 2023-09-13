package com.linkwechat.domain.leads.leads.query;

import com.linkwechat.domain.leads.leads.vo.Properties;
import lombok.Data;

import java.util.List;

/**
 * 线索新增请求参数
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/25 17:24
 */
@Data
public class WeLeadsAddRequest extends WeLeadsBaseRequest {

    /**
     * 性别 0 = 未知, 1 = 男, 2 = 女
     *
     * @see com.linkwechat.common.enums.SexEnums
     */
    private Integer sex;

    /**
     * 自定义属性
     */
    private List<Properties> propertiesList;

}
