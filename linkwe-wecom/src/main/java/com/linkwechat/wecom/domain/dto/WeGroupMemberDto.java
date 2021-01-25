package com.linkwechat.wecom.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/21 0021 23:56
 */
@Data
public class WeGroupMemberDto {
    private static final long serialVersionUID = 1L;

    private String chatId;

    private String memberName;

    private String memberAvatar;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date joinTime;

    /**
     * 入群方式: 1 - 由成员邀请入群（直接邀请入群) ;   2 - 由成员邀请入群（通过邀请链接入群）;  3 - 通过扫描群二维码入群
     */
    private Integer joinScene;

    private Integer joinType;


}
