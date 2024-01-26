package com.linkwechat.fileservice.service.impl;

import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.utils.file.FileUploadUtils;
import com.linkwechat.fileservice.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/**
 * minio存储
 *
 * @author leejoker
 */
@Service
@ConditionalOnProperty(prefix = "linkwechat.file", value = "object", havingValue = "minio")
public class MinioFileServceImpl implements IFileService {

    @Autowired
    private LinkWeChatConfig linkWeChatConfig;

    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        return  linkWeChatConfig.getFile().getCos().getCosImgUrlPrefix()
                + FileUploadUtils.uploadMinio(file, linkWeChatConfig.getFile().getCos());
    }
}
