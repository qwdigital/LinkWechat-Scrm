package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("we_scan_emple_code_count")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeScanEmpleCodeCount {

    @TableId
    private Long id;

    private String externalUserid;

    private String userId;


    private Date createTime;

    private Long empleCodeId;
}
