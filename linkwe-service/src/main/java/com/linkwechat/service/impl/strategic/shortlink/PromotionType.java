package com.linkwechat.service.impl.strategic.shortlink;

import com.linkwechat.domain.WeShortLinkPromotion;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionUpdateQuery;

import java.util.Date;
import java.util.List;

/**
 * 短链推广方式
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/08 16:42
 */
public abstract class PromotionType {

    /**
     * 保存并发送
     *
     * @param query
     * @param weShortLinkPromotion
     * @return
     */
    public abstract Long saveAndSend(WeShortLinkPromotionAddQuery query, WeShortLinkPromotion weShortLinkPromotion);

    /**
     * 更新并发送
     *
     * @param query
     * @param weShortLinkPromotion
     */
    public abstract void updateAndSend(WeShortLinkPromotionUpdateQuery query, WeShortLinkPromotion weShortLinkPromotion);


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
    protected abstract void timingEnd(Long promotionId, Long businessId, Integer type, Date taskEndTime);


}
