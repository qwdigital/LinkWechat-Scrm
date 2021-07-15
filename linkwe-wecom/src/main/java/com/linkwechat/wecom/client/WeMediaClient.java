package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.DataFile;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Query;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * 素材管理
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/24 0024 0:14
 */
@BaseRequest(interceptor = WeAccessTokenInterceptor.class)
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
     * 上传临时素材
     * Inputstream 对象
     * 使用byte数组和Inputstream对象时一定要定义fileName属性
     */
    @Post(url = "/media/upload")
    WeMediaDto upload(@DataFile(value = "media") InputStream file, @Query("type") String type);


}
