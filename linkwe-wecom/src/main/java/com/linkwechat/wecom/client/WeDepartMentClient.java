package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.dto.WeDepartMentDto;
import com.linkwechat.wecom.domain.dto.WeResultDto;

/**
 * @description: 企业微信部门相关客户端
 * @author: HaoN
 * @create: 2020-08-27 16:40
 **/
public interface WeDepartMentClient {

    /**
     * 创建部门
     * @param deartMentDto
     * @return
     */
     @Request(url="/department/create",
             type = "POST"
     )
     WeResultDto createWeDepartMent(@DataObject WeDepartMentDto.DeartMentDto deartMentDto);



    /**
     * 更新部门
     * @param deartMentDto
     * @return
     */
    @Request(url="/department/update",
            type = "POST"
    )
    WeResultDto updateWeDepartMent(@DataObject WeDepartMentDto.DeartMentDto deartMentDto);


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