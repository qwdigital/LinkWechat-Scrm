package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.dtflys.forest.http.ForestResponse;
import com.linkwechat.wecom.domain.dto.GroupWelcomeTplDto;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import com.linkwechat.wecom.interceptor.WeAccessTokenFileInterceptor;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * 素材管理
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/24 0024 0:14
 */
@BaseRequest(baseURL = "${weComServerUrl}${weComePrefix}", interceptor = WeAccessTokenFileInterceptor.class)
public interface WeMediaClient {
    /**
     * 上传图片
     * @param multipartFile
     * @return
     */
    @Post(url="/media/uploadimg")
    WeMediaDto  uploadimg(@DataFile(value = "fieldNameHere") MultipartFile multipartFile);


    /**
     * 上传临时素材
     * Inputstream 对象
     * 使用byte数组和Inputstream对象时一定要定义fileName属性
     */
    @Post(url = "/media/upload")
    WeMediaDto upload(@DataFile(value = "media", fileName = "${1}") InputStream file, String filename, @Query("type") String type);





    /**
     * 上传附件资源
     * Inputstream 对象
     * 使用byte数组和Inputstream对象时一定要定义fileName属性
     */
    @Post(url = "/media/upload_attachment")
    WeMediaDto uploadAttachment(@DataFile(value = "media", fileName = "${1}") InputStream file, String filename, @Query("media_type") String mediaType, @Query("attachment_type") Integer attachmentType);




    /**
     * 获取临时素材
     *
     * @param mediaId 素材id
     * @return
     */
    @GetRequest(url = "/media/get")
    ForestResponse getMediaToResponse(@Query("media_id") String mediaId);


    /**
     * 下载临时素材
     * Inputstream 对象
     *
     */
    @Get(url = "/media/get")
    InputStream mediaGet(@Query("media_id") String media_id);
}
