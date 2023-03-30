package com.linkwechat.fileservice.service.impl;

import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.utils.file.FileUploadUtils;
import com.linkwechat.fileservice.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



/**
 * 集成阿里云oss
 */
@Service
@ConditionalOnProperty(prefix = "linkwechat.file", value = "object", havingValue = "aliOss")
public class AliFileServiceImpl implements IFileService {
    @Autowired
    private LinkWeChatConfig linkWeChatConfig;

    @Override
    public String uploadFile(MultipartFile file) throws Exception {

        return  linkWeChatConfig.getFile().getCos().getCosImgUrlPrefix()+FileUploadUtils
                .uploadAliOss(file, linkWeChatConfig.getFile().getCos());
    }
}
