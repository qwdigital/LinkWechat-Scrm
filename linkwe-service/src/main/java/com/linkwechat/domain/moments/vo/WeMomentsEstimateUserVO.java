package com.linkwechat.domain.moments.vo;

import com.linkwechat.domain.moments.entity.WeMomentsEstimateUser;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 预估朋友圈执行员工
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/26 19:23
 */
@ApiModel("预估朋友圈执行员工")
@Data
public class WeMomentsEstimateUserVO extends WeMomentsEstimateUser {

    /**
     * 执行状态:0未执行，1已执行
     */
    private Integer executeStatus;
}
