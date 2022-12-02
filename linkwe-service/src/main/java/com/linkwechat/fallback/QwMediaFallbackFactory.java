package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.media.WeGetMediaQuery;
import com.linkwechat.domain.wecom.query.media.WeMediaQuery;
import com.linkwechat.domain.wecom.vo.media.WeMediaVo;
import com.linkwechat.fegin.QwMediaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @author leejoker
 * @version 1.0
 * @date 2022/4/2 15:06
 */
@Component
@Slf4j
public class QwMediaFallbackFactory implements QwMediaClient {

    @Override
    public AjaxResult<WeMediaVo> upload(WeMediaQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeMediaVo> webhookUpload(WeMediaQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeMediaVo> uploadImg(WeMediaQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeMediaVo> uploadAttachment(WeMediaQuery query) {
        return null;
    }

    @Override
    public AjaxResult<byte[]> mediaGet(WeMediaQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeMediaVo> getMedia(WeGetMediaQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeMediaVo> uploadAttachment2(WeMediaQuery query) {
        return null;
    }
}
