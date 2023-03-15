package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.Base62NumUtil;
import com.linkwechat.domain.WeShortLinkPromotion;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionUpdateQuery;
import com.linkwechat.domain.shortlink.vo.WeShortLinkPromotionVo;
import com.linkwechat.fegin.QwFileClient;
import com.linkwechat.mapper.WeMaterialMapper;
import com.linkwechat.mapper.WeShortLinkMapper;
import com.linkwechat.mapper.WeShortLinkPromotionMapper;
import com.linkwechat.mapper.WeShortLinkPromotionTemplateClientMapper;
import com.linkwechat.service.IWeShortLinkPromotionAttachmentService;
import com.linkwechat.service.IWeShortLinkPromotionSendResultService;
import com.linkwechat.service.IWeShortLinkPromotionService;
import com.linkwechat.service.impl.strategic.shortlink.PromotionType;
import com.linkwechat.service.impl.strategic.shortlink.ShortLinkPromotionStrategyFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * <p>
 * 短链推广 服务实现类
 * </p>
 *
 * @author WangYX
 * @since 2023-03-07
 */
@Service
public class WeShortLinkPromotionServiceImpl extends ServiceImpl<WeShortLinkPromotionMapper, WeShortLinkPromotion> implements IWeShortLinkPromotionService {

    @Resource
    private WeShortLinkPromotionMapper weShortLinkPromotionMapper;
    @Resource
    private WeMaterialMapper weMaterialMapper;
    @Resource
    private WeShortLinkMapper weShortLinkMapper;
    @Resource
    private LinkWeChatConfig linkWeChatConfig;
    @Resource
    private QwFileClient qwFileClient;
    @Resource
    private WeShortLinkPromotionTemplateClientMapper weShortLinkPromotionTemplateClientMapper;
    @Resource
    private IWeShortLinkPromotionAttachmentService weShortLinkPromotionAttachmentService;
    @Resource
    private IWeShortLinkPromotionSendResultService weShortLinkPromotionSendResultService;

    @Override
    public List<WeShortLinkPromotionVo> selectList(WeShortLinkPromotionQuery query) {
        return weShortLinkPromotionMapper.selectList(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(WeShortLinkPromotionAddQuery query) throws IOException {
        WeShortLinkPromotion weShortLinkPromotion = new WeShortLinkPromotion();
        weShortLinkPromotion.setTaskName(query.getTaskName());
        weShortLinkPromotion.setShortLinkId(query.getShortLinkId());
        weShortLinkPromotion.setStyle(query.getStyle());
        weShortLinkPromotion.setType(query.getType());
        weShortLinkPromotion.setDelFlag(0);
        Optional.ofNullable(query.getMaterialId()).ifPresent(i -> weShortLinkPromotion.setMaterialId(i));

        //推广的二维码或者海报转成附件
        WeMessageTemplate weMessageTemplate = getPromotionUrl(query.getShortLinkId(), query.getStyle(), query.getMaterialId());
        weShortLinkPromotion.setUrl(weMessageTemplate.getPicUrl());
        //添加推广附件
        List<WeMessageTemplate> weMessageTemplates = Optional.ofNullable(query.getAttachmentsList()).orElse(new ArrayList<>());
        weMessageTemplates.add(weMessageTemplate);
        query.setAttachmentsList(weMessageTemplates);
        //保存和发送
        PromotionType promotionType = ShortLinkPromotionStrategyFactory.getPromotionType(query.getType());
        Long id = promotionType.saveAndSend(query, weShortLinkPromotion);
        return id;
    }

    @Override
    public Long ts(WeShortLinkPromotionAddQuery query) throws IOException {
        WeShortLinkPromotion weShortLinkPromotion = BeanUtil.copyProperties(query, WeShortLinkPromotion.class);
        Optional.ofNullable(query.getMaterialId()).ifPresent(i -> weShortLinkPromotion.setMaterialId(i));
        WeMessageTemplate weMessageTemplate = getPromotionUrl(query.getShortLinkId(), query.getStyle(), query.getMaterialId());
        weShortLinkPromotion.setUrl(weMessageTemplate.getPicUrl());
        weShortLinkPromotion.setDelFlag(0);
        weShortLinkPromotion.setTaskStatus(3);
        weShortLinkPromotionMapper.insert(weShortLinkPromotion);
        return weShortLinkPromotion.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(WeShortLinkPromotionUpdateQuery query) {
        Long id = query.getId();
        WeShortLinkPromotion weShortLinkPromotion = weShortLinkPromotionMapper.selectById(id);
        Optional.ofNullable(weShortLinkPromotion).ifPresent(i -> {
            //任务状态: 0带推广 1推广中 2已结束 3暂存中
            if (i.getTaskStatus().equals(1) || i.getTaskStatus().equals(2)) {
                throw new ServiceException("当前状态无法修改！");
            }
            //短链推广
            WeShortLinkPromotion weShortLinkPromotionTemp = BeanUtil.copyProperties(query, WeShortLinkPromotion.class);
            try {
                //推广的二维码或者海报转成附件
                WeMessageTemplate weMessageTemplate = getPromotionUrl(query.getShortLinkId(), query.getStyle(), query.getMaterialId());
                weShortLinkPromotionTemp.setUrl(weMessageTemplate.getPicUrl());
                //添加推广附件
                List<WeMessageTemplate> attachmentsList = query.getAttachmentsList();
                attachmentsList.add(weMessageTemplate);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ServiceException("生成海报异常！");
            }

            //更新和发送
            PromotionType promotionType = ShortLinkPromotionStrategyFactory.getPromotionType(query.getType());
            promotionType.updateAndSend(query, weShortLinkPromotionTemp);
        });
    }

    /**
     * 获取推广图片
     *
     * @param shortLinkId 短链Id
     * @param style       推广样式 0二维码 1海报
     * @param materialId  素材Id
     * @return {@link WeMessageTemplate}
     * @author WangYX
     * @date 2023/03/09 14:50
     */
    private WeMessageTemplate getPromotionUrl(Long shortLinkId, Integer style, Long materialId) throws IOException {
        //获取短链
        String encode = Base62NumUtil.encode(shortLinkId);
        //TODO 这里要加上任务Id
        String shortLinkUrl = linkWeChatConfig.getShortLinkDomainName() + encode;

        WeMessageTemplate weMessageTemplate = new WeMessageTemplate();
        weMessageTemplate.setMsgType("image");

        if (style.equals(0)) {
            //创建二维码，默认宽高160
            BufferedImage image = QrCodeUtil.generate(shortLinkUrl, 160, 160);
            //上传腾讯云
            String url = upload(image, "jpg");
            weMessageTemplate.setPicUrl(url);
            return weMessageTemplate;
        } else {
            //替换海报中的占位码
            Optional.ofNullable(materialId).orElseThrow(() -> new ServiceException("海报素材必填！"));
            WeMaterial weMaterial = weMaterialMapper.selectById(materialId);

            JSONObject jsonObject = Optional.ofNullable(weMaterial)
                    //判断背景图片是否存在
                    .filter(i -> StrUtil.isNotBlank(i.getBackgroundImgUrl()))
                    //判断海报组件是否存在
                    .map(WeMaterial::getPosterSubassembly)
                    .map(i -> JSONObject.parseArray(i))
                    //判断海报是否有占位码组件
                    .filter(i -> i.getJSONObject(0).getInteger("type").equals(3))
                    .map(i -> i.getJSONObject(0))
                    .orElseThrow(() -> new ServiceException("请确认海报是否正确！"));

            //占位码位置
            Integer left = jsonObject.getInteger("left");
            Integer top = jsonObject.getInteger("top");
            //海报宽高
            Integer width = jsonObject.getInteger("width");
            Integer height = jsonObject.getInteger("height");

            //生成二维码
            BufferedImage qrImage = QrCodeUtil.generate(shortLinkUrl, width, height);

            //背景图片
            String backgroundImgUrl = weMaterial.getBackgroundImgUrl();
            //图片后缀
            String suffix = backgroundImgUrl.substring(backgroundImgUrl.lastIndexOf(".") + 1);
            //取代占位码
            URL backgroundImg = new URL(backgroundImgUrl);
            BufferedImage read = ImageIO.read(backgroundImg);
            Graphics2D graphics = read.createGraphics();
            graphics.drawImage(qrImage, left, top, width, height, null);
            String url = upload(read, suffix);
            weMessageTemplate.setPicUrl(url);
            return weMessageTemplate;
        }
    }

    /**
     * 上传图片
     *
     * @param image  图片
     * @param suffix 图片后缀
     * @return {@link String} 上传后的图片地址
     * @throws IOException
     */
    private String upload(BufferedImage image, String suffix) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, suffix, os);
        InputStream input = new ByteArrayInputStream(os.toByteArray());
        String name = UUID.randomUUID().toString() + "." + suffix;
        MultipartFile file = new MockMultipartFile(name, name, null, input);
        AjaxResult<FileEntity> upload = qwFileClient.upload(file);
        if (upload.getCode() == 200) {
            String url = upload.getData().getUrl();
            return url;
        } else {
            throw new ServiceException("文件上传失败");
        }

    }

}
