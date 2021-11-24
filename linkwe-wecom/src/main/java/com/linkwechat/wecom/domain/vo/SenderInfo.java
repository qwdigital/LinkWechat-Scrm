package com.linkwechat.wecom.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SenderInfo implements Serializable {

    private static final long serialVersionUID = 3912731370147355807L;

    /**
     * 跟进员工user_id
     */
    private String userId;

    /**
     * 客户external_userid
     */
    private String customerList;
}
