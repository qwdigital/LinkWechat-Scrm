package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.dto.WeDepartMentDtoDto;
import com.linkwechat.wecom.domain.dto.WeResultDto;

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
     @Request(url="https://qyapi.weixin.qq.com/cgi-bin/department/create",
             type = "POST"
     )
     WeResultDto createWeDepartMent(@DataObject WeDepartMentDtoDto weDepartMent);



    /**
     * 更新部门
     * @param weDepartMent
     * @return
     */
    @Request(url="https://qyapi.weixin.qq.com/cgi-bin/department/update",
            type = "POST"
    )
    WeResultDto updateWeDepartMent(@DataObject WeDepartMentDtoDto weDepartMent);


    /**
     * 通过部门id删除部门 （注：不能删除根部门；不能删除含有子部门、成员的部门）
     * @param departMnentId
     * @return
     */
    @Request(url = "https://qyapi.weixin.qq.com/cgi-bin/department/delete")
    WeResultDto deleteWeDepartMent(@Query("id") String departMnentId);


    /**
     * 获取部门列表
     * @return
     */
    @Request(url = "https://qyapi.weixin.qq.com/cgi-bin/department/list")
    WeDepartMentDtoDto weDepartMents();


}