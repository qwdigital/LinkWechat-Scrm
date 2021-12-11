package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.wecom.domain.dto.WeContactWayDto;
import com.linkwechat.wecom.domain.dto.WeExternalContactDto;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * @description: 员工对外联系方式
 * @author: HaoN
 * @create: 2020-10-13 10:39
 **/
@BaseRequest(baseURL = "${weComServerUrl}${weComePrefix}", interceptor = WeAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeExternalContactClient {

    @Request(url = "/externalcontact/add_contact_way",
            type = "POST"
    )
    WeExternalContactDto addContactWay(@JSONBody WeExternalContactDto.WeContactWay weContactWay);

    @Request(url = "/externalcontact/update_contact_way", type = "POST")
    WeExternalContactDto updateContactWay(@JSONBody WeExternalContactDto.WeContactWay weContactWay);

    @Request(url = "/externalcontact/del_contact_way", type = "POST")
    WeExternalContactDto delContactWay(@JSONBody WeExternalContactDto query);

    @Request(url = "/externalcontact/get_contact_way", type = "POST")
    WeContactWayDto getContactWay(@JSONBody WeExternalContactDto query);
}
