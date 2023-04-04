package com.linkwechat.service.impl.strategic.shortlink;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.Base62NumUtil;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeShortLinkPromotion;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionTaskEndQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionUpdateQuery;
import com.linkwechat.fegin.QwFileClient;
import com.linkwechat.mapper.WeMaterialMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.mock.web.MockMultipartFile;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 短链推广方式
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/08 16:42
 */
public abstract class PromotionType {

    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private RabbitMQSettingConfig rabbitMQSettingConfig;
    @Resource
    private WeMaterialMapper weMaterialMapper;
    @Resource
    private LinkWeChatConfig linkWeChatConfig;
    @Resource
    private QwFileClient qwFileClient;

    /**
     * 保存并发送
     *
     * @param query
     * @param weShortLinkPromotion
     * @return
     */
    public abstract Long saveAndSend(WeShortLinkPromotionAddQuery query, WeShortLinkPromotion weShortLinkPromotion) throws IOException;

    /**
     * 更新并发送
     *
     * @param query
     * @param weShortLinkPromotion
     */
    public abstract void updateAndSend(WeShortLinkPromotionUpdateQuery query, WeShortLinkPromotion weShortLinkPromotion) throws IOException;


    /**
     * 直接发送
     *
     * @param id          短链推广Id
     * @param businessId  短链推广模板Id
     * @param content     短链推广内容
     * @param attachments 短链推广附件
     * @param senderList  短链推广员工
     */
    protected abstract void directSend(Long id, Long businessId, String content, List<WeMessageTemplate> attachments, List<WeAddGroupMessageQuery.SenderInfo> senderList, Object... objects);

    /**
     * 定时发送
     *
     * @param id          短链推广Id
     * @param businessId  短链推广模板Id
     * @param content     短链推广内容
     * @param sendTime    短链推广定时发送时间
     * @param attachments 短链推广附件
     * @param senderList  短链推广员工
     */
    protected abstract void timingSend(Long id, Long businessId, String content, Date sendTime, List<WeMessageTemplate> attachments, List<WeAddGroupMessageQuery.SenderInfo> senderList, Object... objects);

    /**
     * 定时结束
     *
     * @param promotionId 短链推广Id
     * @param businessId  短链推广模板Id
     * @param type        任务所属类型：0群发客户 1群发客群 2朋友圈
     * @param taskEndTime 结束时间
     */
    protected void timingEnd(Long promotionId, Long businessId, Integer type, Date taskEndTime) {
        WeShortLinkPromotionTaskEndQuery query = new WeShortLinkPromotionTaskEndQuery();
        query.setPromotionId(promotionId);
        query.setBusinessId(businessId);
        query.setType(type);

        long diffTime = DateUtils.diffTime(taskEndTime, new Date());
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeDelayGroupMsgEndRk(), JSONObject.toJSONString(query), message -> {
            //注意这里时间可使用long类型,毫秒单位，设置header
            message.getMessageProperties().setHeader("x-delay", diffTime);
            return message;
        });
    }

    /**
     * 获取推广图片
     *
     * @param promotionId 短链推广Id
     * @param shortLinkId 短链Id
     * @param style       推广样式 0二维码 1海报
     * @param materialId  素材Id
     * @return {@link WeMessageTemplate}
     * @author WangYX
     * @date 2023/03/09 14:50
     */
    public WeMessageTemplate getPromotionUrl(Long promotionId, Long shortLinkId, Integer style, Long materialId) throws IOException {
        //获取短链
        String shortLinkIdEncode = Base62NumUtil.encode(shortLinkId);
        //获取短链推广
        String promotionIdEncode = Base62NumUtil.encode(promotionId);

        String shortLinkDomainName = linkWeChatConfig.getShortLinkDomainName();
        boolean b = shortLinkDomainName.startsWith("http");
        if (!b) {
            shortLinkDomainName = "http://" + shortLinkDomainName;
        }
        String shortLinkUrl = shortLinkDomainName + shortLinkIdEncode + "/" + promotionIdEncode;

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
                    .map(i -> i.stream().filter(o -> ((JSONObject) JSON.toJSON(o)).getInteger("type").equals(3)).findFirst())
                    .map(i -> (JSONObject) JSON.toJSON(i))
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
