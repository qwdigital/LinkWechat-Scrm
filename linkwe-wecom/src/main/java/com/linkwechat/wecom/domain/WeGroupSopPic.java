package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("we_group_sop_pic")
public class WeGroupSopPic {
    /**
     * sop id
     */
    private Long ruleId;

    /**
     * 图片url
     */
    private String picUrl;
}
