package com.linkwechat.common.enums.leads.record;

import com.linkwechat.common.exception.ServiceException;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 线索中心-退回方式
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/12 17:32
 */
public enum FollowBackModeEnum {

    MEMBERS_RETURN(0, "成员主动退回"),
    AUTO_RECOVERY(1, "超时未跟进自动回收"),
    ADMIN_RETURN(2, "管理员退回"),
    QUIT_RETURN(3, "离职退回"),
    TRANSFER_RETURN(4, "线索转接退回"),
    ;


    private Integer code;

    private String type;


    FollowBackModeEnum(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public static FollowBackModeEnum ofByCode(Integer code) {
        Optional<FollowBackModeEnum> first = Stream.of(values()).filter(s -> s.code.equals(code)).findFirst();
        return first.orElseThrow(() -> new ServiceException("退回方式不存在！"));
    }
}
