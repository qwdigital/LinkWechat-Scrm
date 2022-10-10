package com.linkwechat.fileservice.service.impl;

import com.linkwechat.fileservice.config.MinioConfig;
import com.linkwechat.fileservice.service.IFileService;
import com.linkwechat.fileservice.utils.FileUploadUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * Minio 文件存储
 *
 * @author leejoker
 */
@Service
@ConditionalOnProperty(prefix = "linkwechat.file", value = "object", havingValue = "minio")
public class MinioFileServiceImpl implements IFileService {
    @Resource
    private MinioConfig minioConfig;

    @Resource
    private MinioClient client;

    /**
     * 本地文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        String fileName = FileUploadUtils.extractFilename(file);
        PutObjectArgs args = PutObjectArgs.builder().bucket(minioConfig.getBucketName()).object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1).contentType(file.getContentType()).build();
        client.putObject(args);
        return minioConfig.getUrl() + "/" + minioConfig.getBucketName() + "/" + fileName;
    }
}
