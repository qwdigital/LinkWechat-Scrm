package com.linkwechat.fileservice.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.linkwechat.common.config.CosConfig;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.fileservice.service.IFileService;
import com.linkwechat.fileservice.utils.VideoFirstFrameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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

    @Resource
    private LinkWeChatConfig linkWeChatConfig;

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

    /**
     * 视频抽帧
     *
     * @param url 视频url
     * @return
     */
    @GetMapping("getVideoFirstImg")
    public AjaxResult<Map<String, Object>> upload(@RequestParam("url") String url) {
        RenderedImage renderedImage = VideoFirstFrameUtils.getFirstFrameRenderedImageByUrl(url);
        try {

            int height = renderedImage.getHeight();
            int width = renderedImage.getWidth();

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(renderedImage, "png", os);
            InputStream inputStream = new ByteArrayInputStream(os.toByteArray());

            MultipartFile multipartFile = new MockMultipartFile("1.png", "2.png", MediaType.IMAGE_PNG_VALUE, inputStream);
            //以字节为单位返回文件的大小,如果为空则为0
            long size = multipartFile.getSize();

            String resultUrl = fileService.uploadFile(multipartFile);

            Map<String, Object> result = new HashMap<>(6);
            result.put("name", FileUtils.getName(url));
            result.put("url", resultUrl);
            result.put("height", height);
            result.put("width", width);
            result.put("memorySize", size);
            result.put("pixelSize", height * width);

            return AjaxResult.success(result);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 腾讯云上传参数
     *
     * @param
     * @return {@link AjaxResult<  Map < String, Object>>}
     * @author WangYX
     * @date 2022/11/01 10:12
     */
    @GetMapping("/get/config")
    public AjaxResult<Map<String, Object>> getCosConfig() {

        //Key length 128/192/256 bits
        String aesKey = linkWeChatConfig.getFile().getAesKey();
        AES aes = SecureUtil.aes(aesKey.getBytes(StandardCharsets.UTF_8));

        CosConfig cos = linkWeChatConfig.getFile().getCos();
        Map<String, String> result = new HashMap<>(5);
        result.put("fileObject", aes.encryptBase64(linkWeChatConfig.getFile().getObject()));
        result.put("secretId", aes.encryptBase64(cos.getSecretId()));
        result.put("secretKey", aes.encryptBase64(cos.getSecretKey()));
        result.put("region", aes.encryptBase64(cos.getRegion()));
        result.put("bucketName", aes.encryptBase64(cos.getBucketName()));
        result.put("cosImgUrlPrefix", aes.encryptBase64(cos.getCosImgUrlPrefix()));


        return AjaxResult.success(result);
    }


}