package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("we_moments_interacte")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeMomentsInteracte {
    private String interacteUserId;
    private Integer interacteUserType;
    private Integer interacteType;
    private Date interacteTime;
    private String momentId;
}
