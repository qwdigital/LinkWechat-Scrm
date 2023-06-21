package com.linkwechat.domain.material.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentDataDetailVo {
    /**
     * 发送者
     */
    private String sendBy;
    /**
     * 查看者
     */
    private String viewBy;
    private Integer sendTotalNum;

    private String externalUserid;
    /**
     * 是否企业用户 0否 1是
     */
    private Integer isCustomer = 0;
    private Integer viewTotalNum;
    private Integer viewByTotalNum;
    private Integer viewDuration = 0;
    private String viewDurationCpt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date viewTime;
    private String viewByOpenid;
    private String viewByUnionid;
    /**
     * 查看者头像
     */
    private String viewAvatar;
}
