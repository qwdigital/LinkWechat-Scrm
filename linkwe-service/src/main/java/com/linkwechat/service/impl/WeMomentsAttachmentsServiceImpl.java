package com.linkwechat.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.moments.dto.MomentsListDetailResultDto;
import com.linkwechat.domain.moments.entity.WeMomentsAttachments;
import com.linkwechat.mapper.WeMomentsAttachmentsMapper;
import com.linkwechat.service.IWeMaterialService;
import com.linkwechat.service.IWeMomentsAttachmentsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 朋友圈附件 服务实现类
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/07 10:09
 */
@Service
public class WeMomentsAttachmentsServiceImpl extends ServiceImpl<WeMomentsAttachmentsMapper, WeMomentsAttachments> implements IWeMomentsAttachmentsService {

    @Resource
    private IWeMaterialService weMaterialService;

    @Override
    public void addMomentsAttachments(Long momentsTaskId, List<Long> materialIds) {
        if (CollectionUtil.isNotEmpty(materialIds)) {
            //验证附件
            validateAttachments(materialIds);
            //添加附件
            List<WeMomentsAttachments> list = new ArrayList<>();
            materialIds.forEach(i -> list.add(build(momentsTaskId, i)));
            this.saveBatch(list);
        }
    }

    /**
     * 构建朋友圈附件
     *
     * @param momentsTaskId 朋友圈任务Id
     * @param materialId    素材Id
     * @return {@link WeMomentsAttachments}
     * @author WangYX
     * @date 2023/06/08 16:50
     */
    private WeMomentsAttachments build(Long momentsTaskId, Long materialId) {
        WeMomentsAttachments weMomentsAttachments = new WeMomentsAttachments();
        weMomentsAttachments.setId(IdUtil.getSnowflake().nextId());
        weMomentsAttachments.setMomentsTaskId(momentsTaskId);
        weMomentsAttachments.setIsMaterial(1);
        weMomentsAttachments.setMaterialId(materialId);
        return weMomentsAttachments;
    }

    @Override
    public void syncAddMomentsAttachments(Long momentsTaskId, MomentsListDetailResultDto.Moment moment) {

        this.remove(new LambdaQueryWrapper<WeMomentsAttachments>()
                .eq(WeMomentsAttachments::getMomentsTaskId,momentsTaskId));
        //图片
        List<MomentsListDetailResultDto.MediaId> images = moment.getImage();
        if (CollectionUtil.isNotEmpty(images)) {
            for (MomentsListDetailResultDto.MediaId image : images) {
                String url = weMaterialService.mediaGet(image.getMedia_id(), MediaType.IMAGE.getType(), "jpg");
                saveImage(momentsTaskId, image.getMedia_id(), url);
            }
        }
        //视频
        MomentsListDetailResultDto.Video video = moment.getVideo();
        if (BeanUtil.isNotEmpty(video)) {
            String mediaUrl = weMaterialService.mediaGet(video.getMedia_id(), MediaType.VIDEO.getType(), "mp4");
            String thumbMediaIdUrl = null;
            if (StrUtil.isNotEmpty(video.getThumb_media_id())) {
                thumbMediaIdUrl = weMaterialService.mediaGet(video.getThumb_media_id(), MediaType.IMAGE.getType(), "jpg");
            }
            saveVideo(momentsTaskId, video.getMedia_id(), mediaUrl, video.getThumb_media_id(), thumbMediaIdUrl);
        }

        //链接
        MomentsListDetailResultDto.Link link = moment.getLink();
        if (BeanUtil.isNotEmpty(link)) {
            saveLink(momentsTaskId, link.getTitle(), link.getUrl());
        }

        //位置
        MomentsListDetailResultDto.Location location = moment.getLocation();
        if (BeanUtil.isNotEmpty(location)) {
            saveLocation(momentsTaskId, location.getLatitude(), location.getLongitude(), location.getName());
        }
    }

    /**
     * 保存图片
     *
     * @param momentsTaskId 朋友圈任务Id
     * @param mediaId       图片的media_id
     * @param mediaIdUrl    图片的media_id对应的url
     * @return
     * @author WangYX
     * @date 2023/06/12 14:37
     */
    private void saveImage(Long momentsTaskId, String mediaId, String mediaIdUrl) {
        WeMomentsAttachments weMomentsAttachments = new WeMomentsAttachments();
        weMomentsAttachments.setId(IdUtil.getSnowflake().nextId());
        weMomentsAttachments.setMomentsTaskId(momentsTaskId);
        weMomentsAttachments.setIsMaterial(0);
        weMomentsAttachments.setMsgType(0);
        weMomentsAttachments.setMediaId(mediaId);
        DateTime dateTime = DateUtil.offsetDay(DateUtil.date(), 3);
        weMomentsAttachments.setMediaIdExpire(dateTime.toLocalDateTime());
        weMomentsAttachments.setMediaIdUrl(mediaIdUrl);
        this.save(weMomentsAttachments);
    }

    /**
     * 保存视频
     *
     * @param momentsTaskId   朋友圈任务Id
     * @param mediaId         视频的media_id
     * @param mediaIdUrl      视频的media_id对应的Url
     * @param thumbMediaId    视频的封面media_id
     * @param thumbMediaIdUrl 视频的封面media_id对应的Url
     * @return
     * @author WangYX
     * @date 2023/06/12 14:39
     */
    private void saveVideo(Long momentsTaskId, String mediaId, String mediaIdUrl, String thumbMediaId, String thumbMediaIdUrl) {
        WeMomentsAttachments weMomentsAttachments = new WeMomentsAttachments();
        weMomentsAttachments.setId(IdUtil.getSnowflake().nextId());
        weMomentsAttachments.setMomentsTaskId(momentsTaskId);
        weMomentsAttachments.setIsMaterial(0);
        weMomentsAttachments.setMsgType(1);
        weMomentsAttachments.setMediaId(mediaId);
        DateTime dateTime = DateUtil.offsetDay(DateUtil.date(), 3);
        weMomentsAttachments.setMediaIdExpire(dateTime.toLocalDateTime());
        weMomentsAttachments.setMediaIdUrl(mediaIdUrl);
        weMomentsAttachments.setThumbMediaId(thumbMediaId);
        weMomentsAttachments.setThumbMediaIdExpire(dateTime.toLocalDateTime());
        weMomentsAttachments.setThumbMediaIdUrl(thumbMediaIdUrl);
        this.save(weMomentsAttachments);
    }

    /**
     * 保存链接
     *
     * @param momentsTaskId 朋友圈任务Id
     * @param title         链接标题
     * @param linkUrl       链接url
     * @return
     * @author WangYX
     * @date 2023/06/12 14:40
     */
    private void saveLink(Long momentsTaskId, String title, String linkUrl) {
        WeMomentsAttachments weMomentsAttachments = new WeMomentsAttachments();
        weMomentsAttachments.setId(IdUtil.getSnowflake().nextId());
        weMomentsAttachments.setMomentsTaskId(momentsTaskId);
        weMomentsAttachments.setIsMaterial(0);
        weMomentsAttachments.setMsgType(2);
        weMomentsAttachments.setLinkTitle(title);
        weMomentsAttachments.setLinkUrl(linkUrl);
        this.save(weMomentsAttachments);
    }

    /**
     * 保存地理位置
     *
     * @param momentsTaskId 朋友圈任务Id
     * @param latitude      经度
     * @param longitude     维度
     * @param name          地理位置名称
     * @return
     * @author WangYX
     * @date 2023/06/12 14:41
     */
    private void saveLocation(Long momentsTaskId, String latitude, String longitude, String name) {
        WeMomentsAttachments weMomentsAttachments = new WeMomentsAttachments();
        weMomentsAttachments.setId(IdUtil.getSnowflake().nextId());
        weMomentsAttachments.setMomentsTaskId(momentsTaskId);
        weMomentsAttachments.setIsMaterial(0);
        weMomentsAttachments.setMsgType(3);
        weMomentsAttachments.setLocationLatitude(latitude);
        weMomentsAttachments.setLocationLongitude(longitude);
        weMomentsAttachments.setLocationName(name);
        this.save(weMomentsAttachments);
    }

    /**
     * 验证附件是否符合要求
     * <p>
     * 1.图片消息附件。普通图片：总像素不超过1555200。图片大小不超过10M。最多支持传入9个；超过9个报错'invalid attachments size'
     * 2.图文封面，普通图片：总像素不超过1555200。可通过上传附件资源接口获得
     *
     * @param materialIds 素材Id集合
     * @return
     * @author WangYX
     * @date 2023/06/27 13:42
     */
    private void validateAttachments(List<Long> materialIds) {
        Long totalPixel = 1555200L;
        List<WeMaterial> weMaterials = weMaterialService.listByIds(materialIds);
        Long pixelSize = 0L;
        for (WeMaterial weMaterial : weMaterials) {
            pixelSize += weMaterial.getPixelSize() != null ? weMaterial.getPixelSize() : 0;
        }
        if (pixelSize > totalPixel) {
            throw new ServiceException("附件中图片总像素不能超过1555200！");
        }
    }


}
