package com.linkwechat.fileservice.controller;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.fileservice.service.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;

/**
 * 文件请求处理
 *
 * @author leejoker
 */
@RestController
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Resource
    private IFileService fileService;

    /**
     * 文件上传请求
     */
    @PostMapping("upload")
    public AjaxResult<FileEntity> upload(@RequestParam("file") MultipartFile file) {
        try {
            // 上传并返回访问地址
            String url = fileService.uploadFile(file);
            FileEntity sysFile = new FileEntity();
            sysFile.setName(FileUtils.getName(url));
            sysFile.setUrl(url);
            return AjaxResult.success(sysFile);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return AjaxResult.error(e.getMessage());
        }
    }
}