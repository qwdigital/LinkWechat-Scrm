package com.linkwechat.fileservice.service.impl;

import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.fileservice.service.IFileService;
import com.linkwechat.fileservice.utils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 本地文件存储
 *
 * @author leejoker
 */
@Service
@ConditionalOnProperty(prefix = "linkwechat.file", value = "object", havingValue = "local")
public class LocalFileServiceImpl implements IFileService {

    @Autowired
    private LinkWeChatConfig linkWeChatConfig;

    /**
     * 本地文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public String uploadFile(MultipartFile file) throws Exception {

        return linkWeChatConfig.getFile().getCos().getCosImgUrlPrefix()
                +FileUploadUtils.upload(linkWeChatConfig.getFile().getCos().getBucketName(), file);
    }
}