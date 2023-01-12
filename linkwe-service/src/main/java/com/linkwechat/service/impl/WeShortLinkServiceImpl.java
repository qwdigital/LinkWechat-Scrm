package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.Base62NumUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.img.NetFileUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.WeShortLink;
import com.linkwechat.domain.shortlink.query.WeShortLinkAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkStatisticQuery;
import com.linkwechat.domain.shortlink.vo.WeShortLinkAddVo;
import com.linkwechat.domain.shortlink.vo.WeShortLinkListVo;
import com.linkwechat.domain.shortlink.vo.WeShortLinkStatisticsVo;
import com.linkwechat.domain.shortlink.vo.WeShortLinkVo;
import com.linkwechat.domain.wecom.query.weixin.WxJumpWxaQuery;
import com.linkwechat.domain.wecom.vo.weixin.WxJumpWxaVo;
import com.linkwechat.fegin.QwFileClient;
import com.linkwechat.fegin.QxAppletClient;
import com.linkwechat.mapper.WeShortLinkMapper;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.service.IWeShortLinkService;
import com.linkwechat.service.IWeShortLinkStatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 短链信息表(WeShortLink)
 *
 * @author danmo
 * @since 2022-12-26 11:07:16
 */
@Slf4j
@Service
public class WeShortLinkServiceImpl extends ServiceImpl<WeShortLinkMapper, WeShortLink> implements IWeShortLinkService {

    @Autowired
    private LinkWeChatConfig linkWeChatConfig;

    @Resource
    private QwFileClient qwFileClient;

    @Resource
    private QxAppletClient qxAppletClient;

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Autowired
    private IWeShortLinkStatService weShortLinkStatService;


    @Override
    public WeShortLinkAddVo addShortLink(WeShortLinkAddQuery query) {
        if (StringUtils.isNotEmpty(query.getLongLink())) {
            if (Objects.equals(1, query.getTouchType()) && !query.isValidUrl(query.getLongLink())) {
                throw new WeComException("无效链接");
            }
        }
        WeShortLinkAddVo weShortLinkAddVo = new WeShortLinkAddVo();
        WeShortLink weShortLink = new WeShortLink();
        BeanUtil.copyProperties(query, weShortLink);

        Integer jumpType = weShortLink.getJumpType();

        Integer touchType = weShortLink.getTouchType();

        Integer type = weShortLink.getType();

        if (save(weShortLink)) {
            String encode = Base62NumUtil.encode(weShortLink.getId());
            String shortLinkUrl = linkWeChatConfig.getShortLinkDomainName() + encode;
            weShortLinkAddVo.setShortUrl(shortLinkUrl);
            //跳转微信
            if (Objects.equals(1, jumpType)) {
                getAppletLink(encode, weShortLink);
                updateById(weShortLink);
            }

            try {
                NetFileUtils.StreamMultipartFile streamMultipartFile = new NetFileUtils.StreamMultipartFile(IdUtil.simpleUUID() + "." + ImgUtil.IMAGE_TYPE_PNG, QrCodeUtil.generatePng(shortLinkUrl, QrConfig.create()));
                FileEntity fileInfo = qwFileClient.upload(streamMultipartFile).getData();
                weShortLinkAddVo.setQrCode(fileInfo.getUrl());
            } catch (Exception e) {
                log.error("生成二维码异常, query={}", JSONObject.toJSONString(query), e);
            }
        }
        return weShortLinkAddVo;
    }

    private void getAppletLink(String encode, WeShortLink entity) {
        WxJumpWxaQuery wxaQuery = new WxJumpWxaQuery();
        WxJumpWxaQuery.JumpWxa wxa = new WxJumpWxaQuery.JumpWxa();
        wxa.setPath(linkWeChatConfig.getShortAppletUrl());
        wxa.setQuery("id=" + encode);
        wxaQuery.setJump_wxa(wxa);
        WxJumpWxaVo wxJumpWxa = qxAppletClient.generateScheme(wxaQuery).getData();
        if (Objects.nonNull(wxJumpWxa) && StringUtils.isNotEmpty(wxJumpWxa.getOpenLink())) {
            entity.setSchemeLink(wxJumpWxa.getOpenLink());
            entity.setTermTime(DateUtil.offsetDay(new Date(), 30));
        } else {
            throw new WeComException("生成小程序跳转链接失败");
        }
    }

    @Override
    public WeShortLinkAddVo updateShortLink(WeShortLinkAddQuery query) {
        if (Objects.isNull(query.getId())) {
            throw new WeComException("ID不能为空");
        }
        if (StringUtils.isNotEmpty(query.getLongLink())) {
            if (Objects.equals(1, query.getTouchType()) && !query.isValidUrl(query.getLongLink())) {
                throw new WeComException("无效链接");
            }
        }
        String encode = Base62NumUtil.encode(query.getId());

        Integer jumpType = query.getJumpType();

        Integer touchType = query.getTouchType();

        Integer type = query.getType();

        WeShortLinkAddVo weShortLinkAddVo = new WeShortLinkAddVo();
        WeShortLink weShortLink = new WeShortLink();
        BeanUtil.copyProperties(query, weShortLink);

        //跳转微信
        if (Objects.equals(1, jumpType) && Objects.equals(2, touchType)) {
            getAppletLink(encode, weShortLink);
        }

        if (updateById(weShortLink)) {
            String shortLinkUrl = linkWeChatConfig.getShortLinkDomainName() + encode;
            weShortLinkAddVo.setShortUrl(shortLinkUrl);
            try {
                NetFileUtils.StreamMultipartFile streamMultipartFile = new NetFileUtils.StreamMultipartFile(IdUtil.simpleUUID() + "." + ImgUtil.IMAGE_TYPE_PNG, QrCodeUtil.generatePng(shortLinkUrl, QrConfig.create()));
                FileEntity fileInfo = qwFileClient.upload(streamMultipartFile).getData();
                weShortLinkAddVo.setQrCode(fileInfo.getUrl());
            } catch (Exception e) {
                log.error("生成二维码异常, query={}", JSONObject.toJSONString(query), e);
            }
        }
        return weShortLinkAddVo;
    }

    @Override
    public void deleteShortLink(List<Long> ids) {
        update(new LambdaUpdateWrapper<WeShortLink>().set(WeShortLink::getDelFlag, 1).in(WeShortLink::getId, ids));
    }

    @Override
    public WeShortLinkVo getShortLinkInfo(Long id) {
        WeShortLinkVo weShortLinkVo = new WeShortLinkVo();
        WeShortLink weShortLink = getById(id);
        BeanUtil.copyProperties(weShortLink, weShortLinkVo);
        String encode = Base62NumUtil.encode(id);
        String shortLinkUrl = linkWeChatConfig.getShortLinkDomainName() + encode;
        weShortLinkVo.setShortLink(shortLinkUrl);
        return weShortLinkVo;
    }

    @Override
    public PageInfo<WeShortLinkListVo> getShortLinkList(WeShortLinkQuery query) {
        PageInfo<WeShortLinkListVo> pageInfo = new PageInfo<>();
        List<WeShortLink> shortLinkList = list(new LambdaQueryWrapper<WeShortLink>()
                .like(StringUtils.isNotEmpty(query.getShortLinkName()), WeShortLink::getShortLinkName, query.getShortLinkName())
                .eq(Objects.nonNull(query.getStatus()), WeShortLink::getStatus, query.getStatus())
                .eq(Objects.nonNull(query.getJumpType()), WeShortLink::getJumpType, query.getJumpType())
                .eq(Objects.nonNull(query.getTouchType()), WeShortLink::getTouchType, query.getTouchType())
                .eq(Objects.nonNull(query.getExtensionType()), WeShortLink::getExtensionType, query.getExtensionType())
                .eq(Objects.nonNull(query.getType()), WeShortLink::getType, query.getType())
        );
        if (CollectionUtil.isNotEmpty(shortLinkList)) {
            List<WeShortLinkListVo> list = shortLinkList.stream().map(shortLink -> {
                WeShortLinkListVo weShortLinkListVo = new WeShortLinkListVo();
                BeanUtil.copyProperties(shortLink, weShortLinkListVo);
                String encode = Base62NumUtil.encode(shortLink.getId());
                String shortLinkUrl = linkWeChatConfig.getShortLinkDomainName() + encode;
                weShortLinkListVo.setShortLink(shortLinkUrl);
                return weShortLinkListVo;
            }).collect(Collectors.toList());
            pageInfo.setList(list);
        }
        PageInfo<WeShortLink> pageIdInfo = new PageInfo<>(shortLinkList);
        pageInfo.setTotal(pageIdInfo.getTotal());
        pageInfo.setPageNum(pageIdInfo.getPageNum());
        pageInfo.setPageSize(pageIdInfo.getPageSize());
        return pageInfo;
    }

    @Override
    public JSONObject getShort2LongUrl(String shortUrl) {
        long id = Base62NumUtil.decode(shortUrl);
        JSONObject resObj = new JSONObject();
        WeShortLink weShortLink = getById(id);
        if (Objects.isNull(weShortLink)) {
            throw new WeComException("无效链接");
        }
        Integer status = weShortLink.getStatus();
        if (Objects.equals(2, status)) {
            throw new WeComException("链接已关闭");
        }
        WeCorpAccount corpAccount = weCorpAccountService.getCorpAccountByCorpId(null);
        if (Objects.isNull(corpAccount)) {
            throw new WeComException("请未配置企业信息");
        }
        if (StringUtils.isEmpty(corpAccount.getWxAppletOriginalId())) {
            throw new WeComException("请未配置小程序原始ID");
        }

        resObj.put("url_scheme", weShortLink.getSchemeLink());
        resObj.put("user_name", corpAccount.getWxAppletOriginalId());
        resObj.put("path", linkWeChatConfig.getShortAppletUrl());
        resObj.put("query", "id=" + shortUrl);
        return resObj;
    }

    @Override
    public WeShortLinkStatisticsVo getDataStatistics(WeShortLinkStatisticQuery query) {
        return weShortLinkStatService.getDataStatistics(query);
    }

    @Override
    public WeShortLinkStatisticsVo getLineStatistics(WeShortLinkStatisticQuery query) {
       return  weShortLinkStatService.getLineStatistics(query);
    }
}
