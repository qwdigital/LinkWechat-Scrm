package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeShortLink;
import com.linkwechat.domain.shortlink.query.WeShortLinkAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkQuery;

import java.util.List;

/**
 * 短链信息表(WeShortLink)
 *
 * @author danmo
 * @since 2022-12-26 11:07:16
 */
public interface IWeShortLinkService extends IService<WeShortLink> {

    void addShortLink(WeShortLinkAddQuery query);

    void updateShortLink(WeShortLinkAddQuery query);

    void deleteShortLink(List<Long> ids);

    void getShortLinkInfo(Long id);

    void getShortLinkList(WeShortLinkQuery query);
}
