package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.fallback.QwFileFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author leejoker
 * @version 1.0
 * @date 2022/4/2 13:42
 */
@FeignClient(value = "${wecom.serve.linkwe-file}", fallback = QwFileFallbackFactory.class)
public interface QwFileClient {

    @PostMapping(value="/file/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    AjaxResult<FileEntity> upload(@RequestPart(value = "file",required = true)  MultipartFile file);
}
