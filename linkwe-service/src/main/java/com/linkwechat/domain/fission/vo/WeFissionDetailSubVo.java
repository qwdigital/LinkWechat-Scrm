package com.linkwechat.domain.fission.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class WeFissionDetailSubVo {
    /**
     *新客
     */
    private String inviterUserName;
    /**
     *被添加目标名群名或员工名
     */
    private String targetName;
    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
}
