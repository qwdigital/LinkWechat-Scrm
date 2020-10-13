package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.DataFile;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Query;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * 素材管理
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/24 0024 0:14
 */
public interface WeMediaClient {

    /**
     * 上传临时素材
     * @param multipartFile
     * @param type
     * @return
     */
    @Post(url="/media/upload")
    WeMediaDto upload(@DataFile(value = "file") MultipartFile multipartFile, @Query("type") String type);

    /**
     * 上传图片
     * @param multipartFile
     * @return
     */
    @Post(url="/media/uploadimg")
    WeMediaDto  uploadimg(@DataFile(value = "file") MultipartFile multipartFile);
}
