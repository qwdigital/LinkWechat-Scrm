package com.linkwechat.domain.fission.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class WeFissionDetailSubVo {
    /**
     *新客
     */
    private String newCustomerName;
    /**
     *添加员工
     */
    private String addUserName;
    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
}
