package com.linkwechat.domain.storecode.vo;

import com.linkwechat.domain.storecode.entity.WeStoreCode;
import lombok.Data;

import java.util.List;

@Data
public class WeStoreCodesVo {
    //加群欢迎语
    private String welcomeMsg;

    private List<WeStoreCode> weStoreCodes;
}
