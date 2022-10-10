package com.linkwechat.domain.wecom.vo.third.auth;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author danmo
 * @description 授权信息
 * @date 2022/3/4 11:34
 **/
@Data
public class WeAuthCorpInfoVo {

    private String corpId;

    private String corpName;

    private String corpType;

    private String corpSquareLogoUrl;

    private Integer corpUserMax;

    private String corpFullName;

    private Integer subjectType;

    private Long verifiedEndTime;

    private String corpWxQrCode;

    private String corpScale;

    private String corpIndustry;

    private String corpSubIndustry;

    private String location;

}
