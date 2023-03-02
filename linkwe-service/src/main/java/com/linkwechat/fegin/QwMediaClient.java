package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.domain.R;
import com.linkwechat.domain.wecom.query.media.WeGetMediaQuery;
import com.linkwechat.domain.wecom.query.media.WeMediaQuery;
import com.linkwechat.domain.wecom.vo.media.WeMediaVo;
import com.linkwechat.fallback.QwMediaFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author leejoker
 * @version 1.0
 * @date 2022/4/2 15:05
 */
@FeignClient(value = "${wecom.serve.linkwe-wecom}", fallback = QwMediaFallbackFactory.class, contextId = "linkwe-wecom-media")
public interface QwMediaClient {

    @PostMapping("/media/upload")
    AjaxResult<WeMediaVo> upload(@RequestBody WeMediaQuery query);

    @PostMapping("/media/webhook/upload")
    AjaxResult<WeMediaVo> webhookUpload(@RequestBody WeMediaQuery query);

    @PostMapping("/media/uploadImg")
    AjaxResult<WeMediaVo> uploadImg(@RequestBody WeMediaQuery query);

    @PostMapping("/media/uploadAttachment")
    AjaxResult<WeMediaVo> uploadAttachment(@RequestBody WeMediaQuery query);

    @GetMapping("/media/mediaGet")
    AjaxResult<byte[]> mediaGet(@SpringQueryMap WeMediaQuery query);

    @PostMapping("/media/getMedia")
    AjaxResult<WeMediaVo> getMedia(@RequestBody WeGetMediaQuery query);

    @PostMapping("/media/uploadAttachment2")
    AjaxResult<WeMediaVo> uploadAttachment2(@RequestBody WeMediaQuery query);
}
