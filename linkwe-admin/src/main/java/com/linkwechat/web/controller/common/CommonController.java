package com.linkwechat.web.controller.common;

import com.linkwechat.common.config.RuoYiConfig;
import com.linkwechat.common.config.ServerConfig;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.FileVo;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.file.FileUploadUtils;
import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.framework.web.domain.server.SysFile;
import com.linkwechat.framework.web.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通用请求处理
 *
 * @author ruoyi
 */
@RestController
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;


    @Autowired
    private FileService fileService;

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.isValidFilename(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;

            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求
     */
    @PostMapping("/common/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/common/download/resource")
    public void resourceDownload(String name, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 本地资源路径
        String localPath = RuoYiConfig.getProfile();
        // 数据库资源地址
        String downloadPath = localPath + StringUtils.substringAfter(name, Constants.RESOURCE_PREFIX);
        // 下载名称
        String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
        FileUtils.writeBytes(downloadPath, response.getOutputStream());
    }


    /**
     * 网络资源通用下载
     */
    @GetMapping("/common/download/url")
    public void webResourceDownload(String url, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FileUtils.downloadFile(url, response.getOutputStream());
    }


    /**
     * 通用上传请求
     */
    @PostMapping("/common/uploadFile2Cos")
    public AjaxResult uploadFile2Cos(MultipartFile file){
        try {

            SysFile sysFile
                    = fileService.upload(file);
            return AjaxResult.success(
                    FileVo.builder()
                            .fileName(sysFile.getFileName())
                            .url(sysFile.getImgUrlPrefix()+sysFile.getFileName())
                            .build()
            );
        } catch (Exception e) {
            return AjaxResult.error("不支持当前文件上传或文件过大建议传20MB以内的文件");
        }
    }


    /**
     * 获取图片
     */
    @GetMapping("/common/findImage")
    public void findImage(HttpServletResponse response,String fileName){
            fileService.findImage(fileName,response);
    }




}
