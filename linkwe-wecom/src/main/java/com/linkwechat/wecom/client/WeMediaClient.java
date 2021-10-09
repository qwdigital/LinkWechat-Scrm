package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
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
@BaseRequest(baseURL = "${weComServerUrl}${weComePrefix}", interceptor = WeAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
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

}
