package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.utils.Base62NumUtil;
import com.linkwechat.domain.WeShortLink;
import com.linkwechat.domain.shortlink.query.WeShortLinkAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkQuery;
import com.linkwechat.mapper.WeShortLinkMapper;
import com.linkwechat.service.IWeShortLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 短链信息表(WeShortLink)
 *
 * @author danmo
 * @since 2022-12-26 11:07:16
 */
@Service
public class WeShortLinkServiceImpl extends ServiceImpl<WeShortLinkMapper, WeShortLink> implements IWeShortLinkSrvice {

    @Autowired
    private LinkWeChatConfig linkWeChatConfig;

    @Override
    public void addShortLink(WeShortLinkAddQuery query) {
        WeShortLink weShortLink = new WeShortLink();
        BeanUtil.copyProperties(query,weShortLink);
        if(save(weShortLink)){
            String encode = Base62NumUtil.encode(weShortLink.getId());
            String shortLinkUrl = linkWeChatConfig.getShortLinkUrl() + encode;
            //QrCodeUtil.(shortLinkUrl);
        }
    }

    @Override
    public void updateShortLink(WeShortLinkAddQuery query) {

    }

    @Override
    public void deleteShortLink(List<Long> ids) {

    }

    @Override
    public void getShortLinkInfo(Long id) {

    }

    @Override
    public void getShortLinkList(WeShortLinkQuery query) {

    }
}
