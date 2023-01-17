package com.linkwechat.wecom.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.http.ForestResponse;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.wecom.query.media.WeGetMediaQuery;
import com.linkwechat.domain.wecom.query.media.WeMediaQuery;
import com.linkwechat.domain.wecom.vo.media.WeMediaVo;
import com.linkwechat.wecom.client.WeMediaClient;
import com.linkwechat.wecom.service.IQwMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Objects;

/**
 * @author danmo
 * @Description 素材业务接口
 * @date 2021/12/16 17:12
 **/
@Slf4j
@Service
public class QwMediaServiceImpl implements IQwMediaService {

    @Resource
    private WeMediaClient weMediaClient;

    @Override
    public WeMediaVo upload(WeMediaQuery query) {
        if (StringUtils.isEmpty(query.getUrl())) {
            throw new WeComException("文件路径不能为空!");
        }
        InputStream inputStream = null;
        try {
            inputStream = new URL(query.getUrl()).openStream();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传临时文件失败......query:{}", JSONObject.toJSONString(query), e);
        }
        return weMediaClient.upload(inputStream,query.getName(), query);
    }

    @Override
    public WeMediaVo uploadImg(WeMediaQuery query) {
        return weMediaClient.uploadimg(query.getFile());
    }

    @Override
    public WeMediaVo uploadAttachment(WeMediaQuery query) {
        if (StringUtils.isEmpty(query.getUrl())) {
            throw new WeComException("附件路径不能为空!");
        }
        InputStream inputStream = null;
        try {
            inputStream = new URL(query.getUrl()).openStream();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传文件失败......query:{}", JSONObject.toJSONString(query), e);
        }
        return weMediaClient.uploadAttachment(inputStream,query.getName(), query);
    }

    @Override
    public InputStream mediaGet(WeMediaQuery query) {
        return weMediaClient.mediaGet(query.getMediaId(), query);
    }

    @Override
    public WeMediaVo getMedia(WeGetMediaQuery query) {
        WeMediaVo weMediaVo = new WeMediaVo();
        weMediaVo.setErrCode(0);
        ForestResponse forestResponse = weMediaClient.getMediaToResponse(query);
        if (forestResponse != null) {
            try {
                String fixedName =new String(forestResponse.getFilename().getBytes("ISO8859-1"),"UTF-8");
                fixedName = fixedName.replace("\"", "");
                if(ObjectUtil.equal("video",forestResponse.getContentType().getType())){
                    fixedName = fixedName + ".mp4";
                }
                weMediaVo.setFileName(fixedName);
                weMediaVo.setBytes(forestResponse.getByteArray());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                weMediaVo.setErrCode(-1);
            } catch (Exception e) {
                e.printStackTrace();
                throw new WeComException("文件下载异常");
            }
        }
        return weMediaVo;
    }

    @Override
    public WeMediaVo uploadAttachment(MultipartFile file, String type, Integer attachmentType) throws IOException {
        WeMediaQuery query = new WeMediaQuery();
        query.setType(type);
        query.setAttachmentType(attachmentType);
        return weMediaClient.uploadAttachment(file.getInputStream(),file.getName(), query);
    }

    @Override
    public WeMediaVo uploadAttachment2(WeMediaQuery query) {
        if (Objects.isNull(query.getFile())) {
            throw new WeComException("附件不能为空!");
        }
        InputStream inputStream = null;
        try {
            inputStream = query.getFile().getInputStream();
        } catch (IOException e) {
            log.error("上传文件失败......query:{}", JSONObject.toJSONString(query), e);
        }
        return weMediaClient.uploadAttachment(inputStream, query.getName(), query);
    }

    @Override
    public WeMediaVo webhookUpload(WeMediaQuery query) {
        if (StringUtils.isEmpty(query.getUrl())) {
            throw new WeComException("文件路径不能为空!");
        }
        InputStream inputStream = null;
        try {
            inputStream = new URL(query.getUrl()).openStream();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传临时文件失败......query:{}", JSONObject.toJSONString(query), e);
        }
        return weMediaClient.webhookUpload(inputStream,query.getName(), query);
    }
}
