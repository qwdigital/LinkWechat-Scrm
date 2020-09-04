package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.dto.WeDepartMentDtoDto;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.domain.dto.WeUserQrcodeDto;

/**
 * @description: 企业微信部门相关客户端
 * @author: HaoN
 * @create: 2020-08-27 16:40
 **/
public interface WeDepartMentClient {

    /**
     * 创建部门
     * @param weDepartMent
     * @return
     */
     @Request(url="/department/create",
             type = "POST"
     )
     WeResultDto createWeDepartMent(@DataObject WeDepartMentDtoDto weDepartMent);



    /**
     * 更新部门
     * @param weDepartMent
     * @return
     */
    @Request(url="/department/update",
            type = "POST"
    )
    WeResultDto updateWeDepartMent(@DataObject WeDepartMentDtoDto weDepartMent);


    /**
     * 通过部门id删除部门 （注：不能删除根部门；不能删除含有子部门、成员的部门）
     * @param departMnentId
     * @return
     */
    @Request(url = "/department/delete")
    WeResultDto deleteWeDepartMent(@Query("id") String departMnentId);


    /**
     * 获取部门列表
     * @return
     */
    @Request(url = "/department/list")
    WeDepartMentDtoDto weDepartMents();


    /**
     * 获取加入企业二维码
     * @param sizeType qrcode尺寸类型，1: 171 x 171; 2: 399 x 399; 3: 741 x 741; 4: 2052 x 2052
     * @return
     */
    @Request(url = " /corp/get_join_qrcode")
    WeUserQrcodeDto getJoinQrcode(@Query("size_type") Integer sizeType);


}