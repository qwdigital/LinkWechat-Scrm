package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.domain.dto.tag.*;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * @description: 企业微信标签相关
 * @author: HaoN
 * @create: 2020-10-17 11:00
 **/
@BaseRequest(baseURL = "${weComServerUrl}${weComePrefix}", interceptor = WeAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeCropTagClient {

    /**
     * 保存标签
     * @param weCropGroupTag  dataType = "json"
     * @return
     */
    @Request(url="/externalcontact/add_corp_tag",
            type = "POST"
    )
    WeCropGropTagDtlDto addCorpTag(@JSONBody WeCropGroupTagDto weCropGroupTag);


    /**
     * 获取所有标签 WeCropGroupTagDto
     * @return
     */
    @Request(url = "/externalcontact/get_corp_tag_list",
            type = "POST"
    )
    WeCropGroupTagListDto getAllCorpTagList();


    /**
     * 根据指定标签的id,获取标签详情
     * @return
     */
    @Request(url = "/externalcontact/get_corp_tag_list",
            type = "POST"
    )
    WeCropGroupTagListDto getCorpTagListByTagIds(@JSONBody WeFindCropTagParam weFindCropTagParam);


    /**
     * 删除企业客户标签
     * @param weCropDelDto
     * @return
     */
    @Request(url = "/externalcontact/del_corp_tag",
            type = "POST"
    )
    WeResultDto delCorpTag(@JSONBody WeCropDelDto weCropDelDto);


    /**
     * 编辑企业微信标签
     * @param weCropTagDto
     * @return
     */
    @Request(url = "/externalcontact/edit_corp_tag",
            type = "POST"
    )
    WeResultDto editCorpTag(@JSONBody WeCropTagDto weCropTagDto);
}