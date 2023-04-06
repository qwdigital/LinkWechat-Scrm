package com.linkwechat.service.impl.strategic.shortlink;

import com.linkwechat.domain.WeShortLinkPromotion;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionUpdateQuery;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 * 默认实现
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/21 17:00
 */
@Service
public class DefaultPromotion extends PromotionType {

    @Override
    public Long saveAndSend(WeShortLinkPromotionAddQuery query, WeShortLinkPromotion weShortLinkPromotion) throws IOException {
        return null;
    }

    @Override
    public void updateAndSend(WeShortLinkPromotionUpdateQuery query, WeShortLinkPromotion weShortLinkPromotion) throws IOException {

    }

    @Override
    protected void directSend(Long id, Long businessId, String content, List<WeMessageTemplate> attachments, List<WeAddGroupMessageQuery.SenderInfo> senderList, Object... objects) {

    }

    @Override
    protected void timingSend(Long id, Long businessId, String content, Date sendTime, List<WeMessageTemplate> attachments, List<WeAddGroupMessageQuery.SenderInfo> senderList, Object... objects) {

    }
}
