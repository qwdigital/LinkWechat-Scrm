package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.WeDepartMent;
import com.linkwechat.wecom.domain.WeResult;

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
     WeResult createWeDepartMent(@DataObject WeDepartMent weDepartMent);



    /**
     * 更新部门
     * @param weDepartMent
     * @return
     */
    @Request(url="https://qyapi.weixin.qq.com/cgi-bin/department/update",
            type = "POST"
    )
     WeResult updateWeDepartMent(@DataObject WeDepartMent weDepartMent);


    /**
     * 通过部门id删除部门 （注：不能删除根部门；不能删除含有子部门、成员的部门）
     * @param departMnentId
     * @return
     */
    @Request(url = "https://qyapi.weixin.qq.com/cgi-bin/department/delete")
    WeResult deleteWeDepartMent(@Query("id") String departMnentId);


    /**
     * 获取部门列表
     * @return
     */
    @Request(url = "https://qyapi.weixin.qq.com/cgi-bin/department/list")
    WeDepartMent weDepartMents();


}