package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.wecom.domain.dto.WeDepartMentDto;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.interceptor.WeCommonAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * @description: 企业微信部门相关客户端
 * @author: HaoN
 * @create: 2020-08-27 16:40
 **/
@BaseRequest(baseURL = "${weComServerUrl}${weComePrefix}", interceptor = WeCommonAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeDepartMentClient {

    /**
     * 创建部门
     * @param deartMentDto
     * @return
     */
     @Request(url="/department/create",
             type = "POST"
     )
     WeResultDto createWeDepartMent(@JSONBody WeDepartMentDto.DeartMentDto deartMentDto);



    /**
     * 更新部门
     * @param deartMentDto
     * @return
     */
    @Request(url="/department/update",
            type = "POST"
    )
    WeResultDto updateWeDepartMent(@JSONBody WeDepartMentDto.DeartMentDto deartMentDto);


    /**
     * 通过部门id删除部门 （注：不能删除根部门；不能删除含有子部门、成员的部门）
     * @param departMnentId
     * @return
     */
    @Request(url = "/department/delete")
    WeResultDto deleteWeDepartMent(@Query("id") String departMnentId);


    /**
     * 获取部门列表
     * @return  @Query("id") Long id
     */
    @Request(url = "/department/list",
            dataType = "json")
    WeDepartMentDto weDepartMents(@Query("id") Long id);



    /**
     * 获取所有部门列表
     * @return
     */
    @Request(url = "/department/list")
    WeDepartMentDto weAllDepartMents();




}