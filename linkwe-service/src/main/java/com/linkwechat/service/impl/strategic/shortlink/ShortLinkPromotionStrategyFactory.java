package com.linkwechat.service.impl.strategic.shortlink;

import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.spring.SpringUtils;

/**
 * 短链策略工厂
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/08 18:13
 */
public class ShortLinkPromotionStrategyFactory {

    /**
     * 获取推广方式
     *
     * @param type 0群发客户，1群发客户群，2群发朋友圈，3应用消息
     * @return
     */
    public static PromotionType getPromotionType(Integer type) {
        assert type != null;
        if (type.equals(ShortLinkPromotionConstants.CLIENT)) {
            return SpringUtils.getBean("clientPromotion", PromotionType.class);
        } else if (type.equals(ShortLinkPromotionConstants.GROUP)) {
            return SpringUtils.getBean("groupPromotion", PromotionType.class);
        } else if (type.equals(ShortLinkPromotionConstants.MOMENTS)) {
            return SpringUtils.getBean("momentsPromotion", PromotionType.class);
        } else if (type.equals(ShortLinkPromotionConstants.APP_MSG)) {
            return SpringUtils.getBean("appMsgPromotion", PromotionType.class);
        } else {
            throw new ServiceException("请检查推广类型!");
        }
    }


}
