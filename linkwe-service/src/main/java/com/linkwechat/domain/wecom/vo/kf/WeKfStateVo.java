package com.linkwechat.domain.wecom.vo.kf;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 客服
 * @date 2021/12/13 10:57
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeKfStateVo extends WeResultVo {

    /**
     * 当前的会话状态
     */
    private Integer serviceState;

    /**
     * 接待人员的userid，仅当state=3时有效
     */
    private String servicerUserid;

    /**
     * 用于发送响应事件消息的code
     */
    private String msgCode;
}
