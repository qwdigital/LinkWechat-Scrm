package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.domain.dto.tag.*;

/**
 * @description: 企业微信标签相关
 * @author: HaoN
 * @create: 2020-10-17 11:00
 **/
public interface WeCropTagClient {

    /**
     * 保存标签
     * @param weCropGroupTag  dataType = "json"
     * @return
     */
    @Request(url="/externalcontact/add_corp_tag",
            type = "POST"
    )
    WeCropGropTagDtlDto addCorpTag(@DataObject WeCropGroupTagDto weCropGroupTag);


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
    WeCropGroupTagListDto getCorpTagListByTagIds(@Body WeFindCropTagParam weFindCropTagParam);


    /**
     * 删除企业客户标签
     * @param weCropDelDto
     * @return
     */
    @Request(url = "/externalcontact/del_corp_tag",
            type = "POST"
    )
    WeResultDto delCorpTag(@DataObject WeCropDelDto weCropDelDto);


    /**
     * 编辑企业微信标签
     * @param weCropTagDto
     * @return
     */
    @Request(url = "/externalcontact/edit_corp_tag",
            type = "POST"
    )
    WeResultDto editCorpTag(@DataObject WeCropTagDto weCropTagDto);
}