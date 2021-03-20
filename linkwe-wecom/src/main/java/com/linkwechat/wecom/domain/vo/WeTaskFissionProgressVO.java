package com.linkwechat.wecom.domain.vo;

import com.linkwechat.wecom.domain.WeCustomer;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/3/20 13:12
 *
 * <p>
 * 客户任务进度和邀请列表
 * </p>
 */
@Data
@Builder
public class WeTaskFissionProgressVO {
    private Long total;
    private Long completed;
    private List<WeCustomer> customers;
}
