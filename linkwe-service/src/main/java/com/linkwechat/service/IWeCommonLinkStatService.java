package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeCommonLinkStat;

import java.util.List;

/**
 * 短链通用统计表(WeCommonLinkStat)
 *
 * @author danmo
 * @since 2023-07-18 13:34:19
 */
public interface IWeCommonLinkStatService extends IService<WeCommonLinkStat> {

    List<WeCommonLinkStat> getStatByShortId(Long shortId, String type);
}
