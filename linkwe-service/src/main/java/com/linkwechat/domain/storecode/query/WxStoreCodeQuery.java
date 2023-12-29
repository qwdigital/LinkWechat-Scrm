package com.linkwechat.domain.storecode.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WxStoreCodeQuery {

    /**
     * 门店码类型(1:门店导购码;2:门店群活码)
     */
   private Integer storeCodeType;

    /**
     * 微信unionid
     */
   private String unionid;

    /**
     * 经度
     */
   private String longitude;

    /**
     * 纬度
     */
   private String latitude;

    /**
     * 区域
     */
   private String area;


    /**
     * 对应的门店活码id
     */
    private Long storeCodeId;

    /**
     * 是否统计 true统计 false不统计
     */
    private Boolean isCount;

}
