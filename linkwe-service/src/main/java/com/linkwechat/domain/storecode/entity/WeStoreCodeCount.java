package com.linkwechat.domain.storecode.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("we_store_code_count")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeStoreCodeCount extends BaseEntity {
    //主键
    @TableId
    private Long id;
    //门店活码id
    private Long storeCodeId;
    //用户的Unionid
    private String unionid;
    //来源(1:导购码;2:群码)
    private Integer source;

    //当前经度
    private String currentLat;

    //当前纬度
    private String currentLng;
}
