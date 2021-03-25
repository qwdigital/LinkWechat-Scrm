package com.linkwechat.wecom.domain.vo;

import com.linkwechat.wecom.domain.WeCustomer;
import lombok.Data;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/3/20 13:50
 */
@Data
public class WeTaskFissionTotalProgressVO {
    private WeCustomer customer;
    private WeTaskFissionProgressVO progress;
}
