package com.linkwechat.wecom.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/21 0021 23:56
 */
@Data
public class WeGroupMember extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "groupId")
    private Long groupId;

    private String memberName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date joinTime;

    private Long joinType;
}
