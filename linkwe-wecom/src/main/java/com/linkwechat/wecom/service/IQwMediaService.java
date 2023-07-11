package com.linkwechat.wecom.service;

import com.linkwechat.domain.wecom.query.media.WeGetMediaQuery;
import com.linkwechat.domain.wecom.query.media.WeMediaQuery;
import com.linkwechat.domain.wecom.vo.media.WeMediaVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author danmo
 * @Description 素材业务接口
 * @date 2021/12/16 17:12
 **/
public interface IQwMediaService {

    /**
     * 上传素材
     * @param query
     * @return
     */
    WeMediaVo upload(WeMediaQuery query);

    /**
     * 上传图片
     * @param query
     * @return
     */
    WeMediaVo uploadImg(WeMediaQuery query);

    /**
     * 上传附件素材
     * @param query
     * @return
     */
    WeMediaVo uploadAttachment(WeMediaQuery query);


    /**
     * 获取临时素材
     * @param query
     * @return
     */
    InputStream mediaGet(WeMediaQuery query);

    WeMediaVo getMedia(WeGetMediaQuery query);

    /**
     * 上传附件素材
     * @param file
     * @param type
     * @param attachmentType
     * @return
     */
    WeMediaVo uploadAttachment(MultipartFile file, String type, Integer attachmentType) throws IOException;

    /**
     * 上传附件素材
     * @param query
     * @return
     */
    WeMediaVo uploadAttachment2(WeMediaQuery query);

    WeMediaVo webhookUpload(WeMediaQuery query);
}
