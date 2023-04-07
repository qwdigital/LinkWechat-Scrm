package com.linkwechat.scheduler.service.impl.welcome;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.enums.WeMsgTypeEnum;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.msgtlp.entity.WeTlpMaterial;
import com.linkwechat.domain.msgtlp.query.WeMsgTlpQuery;
import com.linkwechat.domain.msgtlp.vo.WeMsgTlpVo;
import com.linkwechat.domain.wecom.callback.WeBackCustomerVo;
import com.linkwechat.mapper.WeTlpMaterialMapper;
import com.linkwechat.service.IWeMsgTlpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 默认欢迎语消息
 *
 * @author danmo
 * @date 2023年03月10日 14:38
 */
@Slf4j
@Service
public class WeDefaultQrCodeMsgServiceImpl extends AbstractWelcomeMsgServiceImpl {

    @Autowired
    private IWeMsgTlpService weMsgTlpService;

    @Resource
    private WeTlpMaterialMapper weTlpMaterialMapper;

    @Override
    public void msgHandle(WeBackCustomerVo query) {

        log.info("默认欢迎语欢迎语消息 query：{}", JSONObject.toJSONString(query));

        List<WeMessageTemplate> templates = new ArrayList<>();

        WeMsgTlpQuery weMsgTlpQuery = new WeMsgTlpQuery();
        weMsgTlpQuery.setUserId(query.getUserID());
        weMsgTlpQuery.setFlag(false);
        //员工欢迎语类型
        weMsgTlpQuery.setTplType(2);
        List<WeMsgTlpVo> weMsgTlpList = weMsgTlpService.getList(weMsgTlpQuery);
        if (CollectionUtil.isNotEmpty(weMsgTlpList)) {
            Optional<WeMsgTlpVo> first = weMsgTlpList.stream().findFirst();
            first.ifPresent(o -> {
                WeMsgTlpVo weMsgTlpVo = first.get();
                //文本内容
                WeMessageTemplate weMessageTemplate = new WeMessageTemplate();
                weMessageTemplate.setMsgType(WeMsgTypeEnum.TEXT.getMessageType());
                weMessageTemplate.setContent(weMsgTlpVo.getTemplateInfo());
                templates.add(weMessageTemplate);
                //附件
                LambdaQueryWrapper<WeTlpMaterial> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(WeTlpMaterial::getTlpId, weMsgTlpVo.getId());
                List<WeTlpMaterial> weTlpMaterials = weTlpMaterialMapper.selectList(queryWrapper);
                List<Long> collect = weTlpMaterials.stream().map(x -> x.getMaterialId()).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(collect)) {
                    LambdaQueryWrapper<WeMaterial> wrapper = new LambdaQueryWrapper<>();
                    wrapper.in(WeMaterial::getId, collect);
                    List<WeMaterial> weMaterials = weMaterialService.list(wrapper);
                    if (weMaterials != null && weMaterials.size() > 0) {
                        for (WeMaterial weMaterial : weMaterials) {
                            templates.add(materialToWeMessageTemplate(weMaterial));
                        }
                    }
                }
            });

        }
        sendWelcomeMsg(query, templates);

    }


    /**
     * 素材转消息模板
     *
     * @author WangYX
     * @date 2023/02/03 18:15
     * @version 1.0.0
     */
    private WeMessageTemplate materialToWeMessageTemplate(WeMaterial weMaterial) {
        WeMessageTemplate weMessageTemplate = new WeMessageTemplate();
        String mediaType = weMaterial.getMediaType();
        //
        switch (mediaType) {
            //图片
            //海报
            case "0":
            case "5":
                weMessageTemplate.setMsgType(WeMsgTypeEnum.IMAGE.getMessageType());
                weMessageTemplate.setPicUrl(weMaterial.getMaterialUrl());
                break;

            //小程序
            case "11":
                weMessageTemplate.setMsgType(WeMsgTypeEnum.MINIPROGRAM.getMessageType());
                weMessageTemplate.setTitle(weMaterial.getMaterialName());
                weMessageTemplate.setPicUrl(weMaterial.getCoverUrl());
                weMessageTemplate.setAppId(weMaterial.getDigest());
                weMessageTemplate.setLinkUrl(weMaterial.getMaterialUrl());
                break;
            //图文
            case "9":
                weMessageTemplate.setMsgType(WeMsgTypeEnum.LINK.getMessageType());
                weMessageTemplate.setPicUrl(weMaterial.getCoverUrl());
                weMessageTemplate.setTitle(weMaterial.getMaterialName());
                weMessageTemplate.setDescription(StrUtil.isNotBlank(weMaterial.getContent()) ? weMaterial.getContent() : "");
                weMessageTemplate.setLinkUrl(weMaterial.getMaterialUrl());
                break;
            //文章
            case "12":
                weMessageTemplate.setMsgType(WeMsgTypeEnum.NEWS.getMessageType());
                weMessageTemplate.setPicUrl(weMaterial.getCoverUrl());
                weMessageTemplate.setTitle(weMaterial.getMaterialName());
                weMessageTemplate.setDescription(weMaterial.getDigest());
                weMessageTemplate.setMaterialId(weMaterial.getId());
                break;
            //视频
            case "2":
                weMessageTemplate.setMsgType(WeMsgTypeEnum.VIDEO.getMessageType());
                weMessageTemplate.setPicUrl(weMaterial.getCoverUrl());
                weMessageTemplate.setTitle(weMaterial.getMaterialName());
                weMessageTemplate.setDescription(weMaterial.getDigest());
                weMessageTemplate.setMaterialId(weMaterial.getId());
                weMessageTemplate.setLinkUrl(weMaterial.getMaterialUrl());
                break;
            //文件
            case "3":
                weMessageTemplate.setMsgType(WeMsgTypeEnum.FILE.getMessageType());
                weMessageTemplate.setPicUrl(weMaterial.getCoverUrl());
                weMessageTemplate.setTitle(weMaterial.getMaterialName());
                weMessageTemplate.setDescription(weMaterial.getDigest());
                weMessageTemplate.setMaterialId(weMaterial.getId());
                weMessageTemplate.setLinkUrl(weMaterial.getMaterialUrl());
                break;
        }


        return weMessageTemplate;
    }
}
