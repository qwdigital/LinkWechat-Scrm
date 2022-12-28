package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeShortLink;
import com.linkwechat.mapper.WeShortLinkMapper;
import com.linkwechat.service.IWeShortLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 短链信息表(WeShortLink)
 *
 * @author danmo
 * @since 2022-12-26 11:07:16
 */
@Service
public class WeShortLinkServiceImpl extends ServiceImpl<WeShortLinkMapper, WeShortLink> implements IWeShortLinkService {

}
