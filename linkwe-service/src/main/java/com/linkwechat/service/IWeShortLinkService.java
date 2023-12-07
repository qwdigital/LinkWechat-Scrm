package com.linkwechat.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.linkwechat.domain.WeShortLink;
import com.linkwechat.domain.shortlink.query.WeShortLinkAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkStatisticQuery;
import com.linkwechat.domain.shortlink.vo.WeShortLinkAddVo;
import com.linkwechat.domain.shortlink.vo.WeShortLinkListVo;
import com.linkwechat.domain.shortlink.vo.WeShortLinkStatisticsVo;
import com.linkwechat.domain.shortlink.vo.WeShortLinkVo;

import java.util.List;

/**
 * 短链信息表(WeShortLink)
 *
 * @author danmo
 * @since 2022-12-26 11:07:16
 */
public interface IWeShortLinkService extends IService<WeShortLink> {

    Boolean checkEnv();

    WeShortLinkAddVo addShortLink(WeShortLinkAddQuery query);

    WeShortLinkAddVo updateShortLink(WeShortLinkAddQuery query);

    void deleteShortLink(List<Long> ids);

    WeShortLinkVo getShortLinkInfo(Long id);

    PageInfo<WeShortLinkListVo> getShortLinkList(WeShortLinkQuery query);

    JSONObject getShort2LongUrl(String shortUrl, String promotionIdKey);

    WeShortLinkStatisticsVo getDataStatistics(WeShortLinkStatisticQuery query);

    WeShortLinkStatisticsVo getLineStatistics(WeShortLinkStatisticQuery query);

}
