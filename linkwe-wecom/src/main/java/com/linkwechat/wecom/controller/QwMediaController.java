package com.linkwechat.wecom.controller;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.domain.wecom.query.media.WeGetMediaQuery;
import com.linkwechat.domain.wecom.query.media.WeMediaQuery;
import com.linkwechat.domain.wecom.vo.media.WeMediaVo;
import com.linkwechat.wecom.service.IQwMediaService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author leejoker
 * @version 1.0
 * @date 2022/4/2 15:00
 */
@RestController
@RequestMapping("media")
public class QwMediaController {
    @Resource
    private IQwMediaService iQwMediaService;

    @PostMapping("/upload")
    public AjaxResult<WeMediaVo> upload(@RequestBody WeMediaQuery query) {
        WeMediaVo weMediaVo = iQwMediaService.upload(query);
        return AjaxResult.success(weMediaVo);
    }

    @PostMapping("/webhook/upload")
    public AjaxResult<WeMediaVo> webhookUpload(@RequestBody WeMediaQuery query) {
        WeMediaVo weMediaVo = iQwMediaService.webhookUpload(query);
        return AjaxResult.success(weMediaVo);
    }

    @PostMapping("/uploadImg")
    public AjaxResult<WeMediaVo> uploadImg(@Validated @RequestBody WeMediaQuery query) {
        return AjaxResult.success(iQwMediaService.uploadImg(query));
    }

    @PostMapping("/uploadAttachment")
    public AjaxResult<WeMediaVo> uploadAttachment(@RequestBody WeMediaQuery query) {
        return AjaxResult.success(iQwMediaService.uploadAttachment(query));
    }

    @PostMapping("/uploadAttachment2")
    public AjaxResult<WeMediaVo> uploadAttachment2(@RequestBody WeMediaQuery query) {
        return AjaxResult.success(iQwMediaService.uploadAttachment2(query));
    }

    @GetMapping("/mediaGet")
    public AjaxResult<byte[]> mediaGet(WeMediaQuery query) throws IOException {


        return AjaxResult.success(FileUtils.inputTobyte(iQwMediaService.mediaGet(query)));
    }

    @PostMapping("/getMedia")
    public AjaxResult<WeMediaVo> getMedia(@Validated @RequestBody WeGetMediaQuery query){
        return AjaxResult.success(iQwMediaService.getMedia(query));
    }
}
