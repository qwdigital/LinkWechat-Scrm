package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.domain.R;
import com.linkwechat.fegin.QwFileClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author leejoker
 * @version 1.0
 * @date 2022/4/2 13:46
 */
@Component
@Slf4j
public class QwFileFallbackFactory implements QwFileClient {
    @Override
    public AjaxResult<FileEntity> upload(MultipartFile file) {
        return null;
    }
}
