package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.dto.WeExternalContactDto;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;

/**
 * @description: 员工对外联系方式
 * @author: HaoN
 * @create: 2020-10-13 10:39
 **/
@BaseRequest(interceptor = WeAccessTokenInterceptor.class)
public interface WeExternalContactClient {

    @Request(url = "/externalcontact/add_contact_way",
            type = "POST"
    )
    WeExternalContactDto addContactWay(@DataObject WeExternalContactDto.WeContactWay weContactWay);

    @Request(url = "/externalcontact/update_contact_way", type = "POST")
    WeExternalContactDto updateContactWay(@DataObject WeExternalContactDto.WeContactWay weContactWay);

    @Request(url = "/externalcontact/del_contact_way", type = "POST")
    WeExternalContactDto delContactWay(@Query("configId") String configId);

    @Request(url = "/externalcontact/get_contact_way", type = "POST")
    WeExternalContactDto getContactWay(@Query("configId") String configId);
}
