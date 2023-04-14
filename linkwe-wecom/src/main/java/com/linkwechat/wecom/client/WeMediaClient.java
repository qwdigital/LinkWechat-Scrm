package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.dtflys.forest.http.ForestResponse;
import com.linkwechat.domain.wecom.query.media.WeGetMediaQuery;
import com.linkwechat.domain.wecom.query.media.WeMediaQuery;
import com.linkwechat.domain.wecom.vo.media.WeMediaVo;
import com.linkwechat.wecom.interceptor.WeAccessTokenFileInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 素材管理
 *
 * @Author: leejoker
 */
@BaseRequest(baseURL = "${weComServerUrl}", interceptor = WeAccessTokenFileInterceptor.class, connectTimeout = 10000, readTimeout = 10000)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeMediaClient {
    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @Post(url = "/media/uploadimg")
    WeMediaVo uploadimg(@DataFile(value = "fieldNameHere") MultipartFile file);


    /**
     * 上传临时素材
     * Inputstream 对象
     * 使用byte数组和Inputstream对象时一定要定义fileName属性
     */
    @LogEnabled(value = false)
    @Post(url = "/media/upload?type=${query.type}")
    WeMediaVo upload(@DataFile(value = "media", fileName = "${1}") InputStream file,String name,
                     @Var("query") WeMediaQuery query);

    /**
     * 上传临时素材
     * 使用byte数组和Inputstream对象时一定要定义fileName属性
     */
    @Post(url = "/media/upload?type=${query.type}")
    WeMediaVo upload(@DataFile(value = "media") MultipartFile file,@Var("query") WeMediaQuery query);


    /**
     * 上传附件资源
     * Inputstream 对象
     * 使用byte数组和Inputstream对象时一定要定义fileName属性
     */
    @Post(url = "/media/upload_attachment?media_type=${query.type}&attachment_type=${query.attachmentType}")
    WeMediaVo uploadAttachment(@DataFile(value = "media", fileName = "${1}") InputStream file,String name,
                               @Var("query") WeMediaQuery query);

    /**
     * 获取临时素材
     *
     * @param query 素材id
     * @return
     */
    @GetRequest(url = "/media/get?media_id=${query.mediaId}")
    ForestResponse getMediaToResponse(@Var("query") WeGetMediaQuery query);


    /**
     * 下载临时素材
     * Inputstream 对象
     */
    @Get(url = "/media/get")
    InputStream mediaGet(@Query("media_id") String media_id,@Var("query") WeMediaQuery query);

    /**
     * 群机器人文件上传
     * @param file
     * @param name
     * @param query
     * @return
     */
    @LogEnabled(value = false)
    @Post(url = "/webhook/upload_media?key={query.key}&type=${query.type}",interceptor = {})
    WeMediaVo webhookUpload(@DataFile(value = "media", fileName = "${1}") InputStream file,String name,
                     @Var("query") WeMediaQuery query);
}
